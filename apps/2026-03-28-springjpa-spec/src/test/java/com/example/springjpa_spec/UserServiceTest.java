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

        List<User> result = userService.search(null, null);

        assertThat(result).isEmpty();
        verify(userRepo, never()).findAll(ArgumentMatchers.<Specification<User>>any());
    }
}
