package com.example.userapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(

  @Email(message = "メール形式が正しくありません")
  @NotBlank(message = "メールアドレスは必須です")
  String email,

  @NotBlank(message = "パスワードは必須です")
  String password,

  @NotBlank(message = "名前は必須です")
  String name
) {}