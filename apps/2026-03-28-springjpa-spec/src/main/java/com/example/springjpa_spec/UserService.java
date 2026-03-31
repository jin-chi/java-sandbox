package com.example.springjpa_spec;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepo;

    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public List<User> search(String name, Integer age) {
        if (name == null && age == null) {
            return List.of();
        }
        Specification<User> spec = Specification
                                .where(UserSpecifications.hasName(name)
                                .and(UserSpecifications.hasAge(age)));
                                
        return userRepo.findAll(spec);
    }
}
