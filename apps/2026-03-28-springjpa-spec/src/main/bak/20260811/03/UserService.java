package com.example.springjpa_spec;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> search(String name, Integer age) {
        if (!StringUtils.hasText(name) && age == null)
            return List.of();

        Specification<User> spec = UserSpecifications.nameContains(name)
                .and(UserSpecifications.ageEquals(age));

        List<UserResponseDto> result = userRepository.findAll(spec).stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return result;
    }
}