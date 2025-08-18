package com.example.userapi.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userapi.dto.LoginRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class AuthController {
  
  private final AuthenticationManager authManager;

  /** 認証してセッションを開始 */
  @PostMapping("/login")
  public Authentication login(HttpServletRequest request, @RequestBody @Valid LoginRequest req) {
    Authentication auth = authManager.authenticate(
      new UsernamePasswordAuthenticationToken(req.email(), req.password())
    );

    // 1. SecurityContext にセット
    SecurityContextHolder.getContext().setAuthentication(auth);

    // 2. Session を確保し SecurityContext を保存
    request.getSession(true) // true: なければ作成
           .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                         SecurityContextHolder.getContext());

    return auth; // Swagger で確認しやすいよう認証オブジェクトを返す
  }

  /** ログインユーザー情報を返すサンプル */
  @GetMapping("/me")
  public Object me(Authentication auth) { // Spring が注入
    return auth;
  }
}
