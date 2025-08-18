package com.example.userapi.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController // 戻り値をJSONで返す
@RequestMapping("/users") // base path
@RequiredArgsConstructor
@Validated
public class UserController {

  private final UserService service;

  /* 登録 */
  @PostMapping
  public UserResponse register(@RequestBody @Valid UserRequest req) {
    return service.register(req);
  }
}