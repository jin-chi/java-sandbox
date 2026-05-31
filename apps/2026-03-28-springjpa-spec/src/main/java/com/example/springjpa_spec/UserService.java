package com.example.springjpa_spec;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    // UserRepositoryヲDIする
    private UserRepository userRepository;

    // name, ageを条件にUserを検索して返す
    // 両方nullの場合は空リストを返す
    public List<User> search(String name, Integer age) {
        if ((name == null || name.isEmpty()) && age == null) return null;

        Specification<User> spec = Specification
                                        .where(UserSpecifications.hasName(name))
                                        .and(UserSpecifications.hasAge(age));
        return userRepository.findAll(spec);
    }
}
