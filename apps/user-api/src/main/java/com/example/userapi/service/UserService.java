package com.example.userapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userapi.domain.User;
import com.example.userapi.dto.LoginRequest;
import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository repo;

  /* 登録 */
  public UserResponse register(UserRequest req) {
    if (repo.existsByEmail(req.email())) {
      throw new IllegalArgumentException("メールアドレスは既に登録済みです");
    }
    String hash = passwordEncoder.encode(req.password());
    User saved = repo.save(User.of(req.email(), req.name(), hash));
    return new UserResponse(saved.getId(), saved.getEmail(), saved.getName());
  }

  /* ログイン認証 */
  public UserResponse login(LoginRequest req) {
    User u = repo.findByEmail(req.email())
                 .orElseThrow(() -> new SecurityException("メールまたはパスワードが不正です"));

    if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
      throw new SecurityException("メールまたはパスワードが不正です");
    }
    return new UserResponse(u.getId(), u.getEmail(), u.getName());
  }

}
