package com.sb02.blogdemo.user.core.entity;

import com.sb02.blogdemo.user.core.PasswordUtil;
import com.sb02.blogdemo.user.core.exception.UserRegisterException;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String password;
    private String email;
    private String nickname;
    private Instant createdAt;

    private User(
            String id,
            String password,
            String email,
            String nickname,
            Instant createdAt
    ) {
        validate(id, password, email, nickname);
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.password = encryptPassword(password);
    }

    public static User create(String id, String password, String email, String nickname) {
        return new User(id, password, email, nickname, Instant.now());
    }

    private void validate(String id, String password, String email, String nickname) {
        validateId(id);
        validatePassword(password);
        validateEmail(email);
        validateNickname(nickname);
    }

    private void validateId(String id) {
        // ID: 6-30글자 이하
        if (id.length() < 6 || id.length() > 30) {
            throw new UserRegisterException("Register failed, invalid id");
        }
    }

    private void validatePassword(String password) {
        // 비밀번호: 12-50글자, 영문/숫자/특수문자(!@#$%^&*) 각 2글자 이상 포함
        String regex = "^(?=(?:.*[a-zA-Z]){2,})(?=(?:.*[0-9]){2,})(?=(?:.*[!@#$%^&*]){2,}).{12,50}$";
        if (!password.matches(regex)) {
            throw new UserRegisterException("Register failed, invalid password");
        }
    }

    private void validateEmail(String email) {
        // 이메일: 이메일 형식, 100글자 이하
        if (email.length() > 100 || !email.contains("@")) {
            throw new UserRegisterException("Register failed, invalid email");
        }
    }

    private void validateNickname(String nickname) {
        // 닉네임: 3-50글자 이하
        if (nickname.length() < 3 || nickname.length() > 50) {
            throw new UserRegisterException("Register failed, invalid nickname");
        }
    }

    private String encryptPassword(String password) {
        try {
            return PasswordUtil.encryptPassword(password);
        } catch (IllegalArgumentException e) {
            throw new UserRegisterException("Register failed, password encryption failed");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
