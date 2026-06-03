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

    // nameもageもnullのとき、空リストを返しfindAllを呼ばない
    @Test
    void search_returnsEmptyList_whenBothParamsAreNull() {
        List<User> result = userService.search(null, null);

        // テストは assertThat() を使う
        assertThat(result).isEmpty();

        // findAll() が呼ばれていないことを明示的に確認する
        verify(userRepo, never()).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    // nameのみ指定のとき、findAllを呼び結果を返す
    @Test
    void search_callsFindAll_whenOnlyNameIsGiven() {
        // List は List.of(mockUser) で簡潔に書ける
        User mockUser = User.builder()
                            .name("taro")
                            .build();
        when(userRepo.findAll(ArgumentMatchers.<Specification<User>>any())).thenReturn(List.of(mockUser));

        List<User> result = userService.search("taro", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("taro");
        // name を確認するテストなので age の null チェックは不要
        // assertThat(result.get(0).getAge()).isEqualTo(null);

        // time(1) は省略できる。デフォルトなので。
        verify(userRepo).findAll(ArgumentMatchers.<Specification<User>>any());
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
