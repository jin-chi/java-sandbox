package com.example.usersbasic;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
class UserRepositoryTests {
    
    @Autowired
    UserRepository repo;

    @Test
    @DisplayName("create: 保存するとIDが採番される")
    void createUser_assignsId() {
        User u = new User();
        u.setEmail("taro@example.com");
        u.setName("Taro");

        User saved = repo.saveAndFlush(u);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("taro@example.com");
    }

    @Test
    @DisplayName("read: IDで取得できる")
    void findById_returns() {
        User u = new User();
        u.setEmail("jiro@example.com");
        u.setName("Jiro");
        User saved = repo.saveAndFlush(u);

        Optional<User> found = repo.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jiro");
    }

    @Test
    @DisplayName("update: フィールドを更新できる")
    void update_changesFields() {
        User u = new User();
        u.setEmail("hanako@Example.com");
        u.setName("Hanako");
        User saved = repo.saveAndFlush(u);

        saved.setName("Hanako Updated");
        User updated = repo.saveAndFlush(saved);

        assertThat(updated.getName()).isEqualTo("Hanako Updated");
    }

    @Test
    @DisplayName("update: フィールドを更新できる")
    void delete_removes() {
        User u = new User();
        u.setEmail("delete@example.com");
        u.setName("Delete");
        User saved = repo.saveAndFlush(u);

        repo.deleteById(saved.getId());
        repo.flush();

        assertThat(repo.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("validation: 必須/形式違反は保存時に例外（@NotBlank/@Email）")
    void validation_validations() {
        User u = new User();
        u.setEmail("");
        u.setName("NoEmail");

        assertThatThrownBy(() -> repo.saveAndFlush(u))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("conflict: emailユニーク制約違反で409相当（DataIntegrityViolationException）")
    void duplicateEmail_conflict() {
        User a = new User();
        a.setEmail("dup@example.com");
        a.setName("A");
        repo.saveAndFlush(a);

        User b = new User();
        b.setEmail("dup@example.com");
        b.setName("B");

        assertThatThrownBy(() -> repo.saveAndFlush(b))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
