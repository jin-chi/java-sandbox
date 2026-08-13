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

    public List<UserResponseDto> search(UserSearchRequest req) {
        if (req.isEmpty()) return List.of();

        Specification<User> spec = UserSpecifications.nameContains(req.getName())
                .and(UserSpecifications.emailContains(req.getEmail()))
                .and(UserSpecifications.ageEquals(req.getAge()))
                .and(UserSpecifications.keywordContains(req.getKeyword()))
                .and(UserSpecifications.ageFrom(req.getAgeFrom()))
                .and(UserSpecifications.ageTo(req.getAgeTo()));

        List<UserResponseDto> result = userRepository.findAll(spec).stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return result;
    }
}
