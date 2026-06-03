package com.example.springjpa_spec;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepo;

    @InjectMocks
    UserService userService;

    // nameもageもnullのとき、空リストを返しfindAllを呼ばない
    @Test
    void search_returnsEmptyList_whenBothParamsAreNull() {
        List<User> result = userService.search(null, null);

        assertThat(result).isEmpty();
        verify(userRepo, never()).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    // nameのみ指定のとき、findAllを呼び結果を返す
    @Test
    void search_callsFindAll_whenOnlyNameIsGiven() {
        User alice = User.builder().id(1L).name("Alice").age(30).build();
        when(userRepo.findAll(ArgumentMatchers.<Specification<User>>any())).thenReturn(List.of(alice));

        List<User> result = userService.search("Alice", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
        verify(userRepo).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    // ageのみ指定のとき、findAllを呼び結果を返す
    @Test
    void search_callsFindAll_whenOnlyAgeIsGiven() {
        User alice = User.builder().id(1L).name("Alice").age(30).build();
        when(userRepo.findAll(ArgumentMatchers.<Specification<User>>any())).thenReturn(List.of(alice));

        List<User> result = userService.search(null, 30);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAge()).isEqualTo(30);
        verify(userRepo).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    // nameとage両方指定のとき、findAllを呼び結果を返す
    @Test
    void search_callsFindAll_whenBothParamsAreGiven() {
        User alice = User.builder().id(1L).name("Alice").age(30).build();
        when(userRepo.findAll(ArgumentMatchers.<Specification<User>>any())).thenReturn(List.of(alice));

        List<User> result = userService.search("Alice", 30);

        assertThat(result).hasSize(1);
        verify(userRepo).findAll(ArgumentMatchers.<Specification<User>>any());
    }
}
