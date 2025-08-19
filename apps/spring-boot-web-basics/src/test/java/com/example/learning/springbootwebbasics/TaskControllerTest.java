package com.example.learning.springbootwebbasics;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        // 各テスト実行前にデータベースをクリアにする
        taskRepository.deleteAll();
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        // given
        TaskRequest taskRequest = new TaskRequest("テストタスク", "テスト用のタスクです。", false);

        // when
        MvcResult result = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        String content = result.getResponse().getContentAsString();
        Task createdTask = objectMapper.readValue(content, Task.class);

        assertNotNull(createdTask.getId());
        assertEquals("テストタスク", createdTask.getTitle());
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        // given
        Task task = new Task("取得テスト", "IDで取得するタスク", false);
        Task savedTask = taskRepository.save(task);

        // when
        MvcResult result = mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String content = result.getResponse().getContentAsString();
        Task foundTask = objectMapper.readValue(content, Task.class);

        assertEquals(savedTask.getId(), foundTask.getId());
        assertEquals("取得テスト", foundTask.getTitle());
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        // given
        Long nonExistentId = 999L;

        // when & then
        mockMvc.perform(get("/api/tasks/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
