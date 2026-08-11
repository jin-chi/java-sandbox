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
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @Test
    void search_returnsEmptyList_whenBothParamsAreNull() {
        // 結果はないのでモックの結果は作らない
        // モックも作らないので when() もなし

        List<UserResponseDto> result = userService.search(null, null, null, null);

        assertThat(result).isEmpty();
        verify(userRepo, never()).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    @Test
    void search_callsFindAll_whenOnlyNameIsGiven() {
        User mockUser = User.builder()
                            .name("taro")
                            .build();
        when(userRepo.findAll(ArgumentMatchers.<Specification<User>>any())).thenReturn(List.of(mockUser));

        List<UserResponseDto> result = userService.search("taro", null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("taro");
        verify(userRepo).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    @Test
    void search_callsFindAll_whenOnlyAgeIsGiven() {
        User mockUser = User.builder()
                            .age(30)
                            .build();
        when(userRepo.findAll(ArgumentMatchers.<Specification<User>>any())).thenReturn(List.of(mockUser));

        List<UserResponseDto> result = userService.search(null, 30, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAge()).isEqualTo(30);
        verify(userRepo).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    @Test
    void search_callsFindAll_whenBothParamsAreGiven() {
        User mockUser = User.builder()
                            .name("taro")
                            .age(30)
                            .build();
        when(userRepo.findAll(ArgumentMatchers.<Specification<User>>any())).thenReturn(List.of(mockUser));

        List<UserResponseDto> result = userService.search("taro", 30, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("taro");
        assertThat(result.get(0).getAge()).isEqualTo(30);
        verify(userRepo).findAll(ArgumentMatchers.<Specification<User>>any());
    }
}
