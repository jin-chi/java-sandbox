package com.example.springjpa_spec;

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

        result.isEmpty();
        verify(userRepo, never()).findAll(ArgumentMatchers.<Specification<User>>any());
    }

    // nameのみ指定のとき、findAllを呼び結果を返す
    @Test
    void search_callsFindAll_whenOnlyNameIsGiven() {

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
