package com.example.userapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
  
  @Email(message = "メール形式が正しくありません")
  @NotBlank(message = "メールアドレスは必須です")
  String email,

  @NotBlank(message = "パスワードは必須です")
  String password
) {}
