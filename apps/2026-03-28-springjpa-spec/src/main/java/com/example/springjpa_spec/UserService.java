package com.example.springjpa_spec;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> search(String name, Integer age) {
        if ((name == null || name.isEmpty()) && age == null) return List.of();
        Specification<User> spec = Specification
                                        .where(UserSpecifications.hasName(name))
                                        .and(UserSpecifications.hasAge(age));
        return userRepository.findAll(spec)
                .stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }
}
