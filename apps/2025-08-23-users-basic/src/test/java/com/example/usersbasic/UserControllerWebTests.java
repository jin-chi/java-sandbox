package com.example.usersbasic;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class)
@Import(GlobalExceptionHamdler.class)
class UserControllerWebTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserRepository repo;

    private static User user(Long id, String email, String name) {
        User u = new User();
        u.setEmail(email);
        u.setName(name);
        if (id != null) {
            u.setId(id);
        }
        return u;
    }

    @Test
    void post_creates_201_with_Location() throws Exception {
        User in = user(null, "taro@example.com", "Taro");
        when(repo.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("taro@example.com"));
    }

    @Test
    void post_validationError_returns_400() throws Exception {
        String body = """
                { "name": "NoEmail" }
                """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void post_duplicateEmail_returns_409() throws Exception {
        when(repo.save(any(User.class))).thenThrow(
                new org.springframework.dao.DataIntegrityViolationException("dup"));

        String body = """
                { "email":"dup@example.com", "name":"Dup" }
                """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void get_found_returns_200() throws Exception {
        when(repo.findById(1L)).thenReturn(Optional.of(user(1L, "taro@example.com", "Taro")));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("taro@example.com"));
    }

    @Test
    void get_notFound_returns_404() throws Exception {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void put_updates_returns_200() throws Exception {
        when(repo.findById(1L)).thenReturn(Optional.of(user(1L, "old@example.com", "Old")));
        when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        String body = """
                { "email":"new@example.com", "name":"New" }
                """;
        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void delete_existing_returns_204() throws Exception {
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound_returns_404() throws Exception {
        when(repo.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound());
    }
}
