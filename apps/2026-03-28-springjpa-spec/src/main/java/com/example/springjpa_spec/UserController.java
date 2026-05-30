package com.example.springjpa_spec;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  
    // UserServiceをDIする
    UserService userService;

    // GETエンドポイント。name, ageは任意のクエリパラメータ
    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age) {
        return ResponseEntity.ok(userService.search(name, age));

    }
}
