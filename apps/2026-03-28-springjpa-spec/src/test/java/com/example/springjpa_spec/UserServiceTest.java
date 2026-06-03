package com.example.springjpa_spec;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
        List<User> users = new ArrayList<>();
        User mockUser = User.builder()
                            .name("taro")
                            .build();
        users.add(mockUser);
        when(userRepo.findAll(ArgumentMatchers.<Specification<User>>any())).thenReturn(users);

        List<User> result = userService.search("taro", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("taro");
        assertThat(result.get(0).getAge()).isEqualTo(null);
        verify(userRepo, times(1)).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    // argのみ指定のとき、findAllを呼び結果を返す
    @Test
    void search_callsFindAll_whenOnlyAgeIsGiven() {

    }

    // nameとage両方指定のとき、findAllを呼び結果を返す
    @Test
    void search_callsFindAll_whenBothParamsAreGiven() {

    }
}
