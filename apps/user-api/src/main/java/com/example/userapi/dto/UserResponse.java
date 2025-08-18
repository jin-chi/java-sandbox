package com.example.userapi.dto;

public record UserResponse(
  Long id,
  String email,
  String name
) {}