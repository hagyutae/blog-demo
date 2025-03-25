package com.sb02.blogdemo.core.user.entity;

import com.sb02.blogdemo.core.user.PasswordUtil;
import com.sb02.blogdemo.core.user.exception.UserRegisterException;
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
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.password = encryptPassword(password);
    }

    public static User create(String id, String password, String email, String nickname) {
        Validator.validate(id, password, email, nickname);
        return new User(id, password, email, nickname, Instant.now());
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

    private static class Validator {
        public static void validate(String id, String password, String email, String nickname) {
            validateId(id);
            validatePassword(password);
            validateEmail(email);
            validateNickname(nickname);
        }

        public static void validateId(String id) {
            if (id == null) {
                throw new UserRegisterException("Register failed, empty id");
            }
            if (id.length() < 6 || id.length() > 30) {
                throw new UserRegisterException("Register failed, invalid id");
            }
        }

        public static void validatePassword(String password) {
            if (password == null) {
                throw new UserRegisterException("Register failed, empty password");
            }
            // 비밀번호: 12-50글자, 영문/숫자/특수문자(!@#$%^&*) 각 2글자 이상 포함
            String regex = "^(?=(?:.*[a-zA-Z]){2,})(?=(?:.*[0-9]){2,})(?=(?:.*[!@#$%^&*]){2,}).{12,50}$";
            if (!password.matches(regex)) {
                throw new UserRegisterException("Register failed, invalid password");
            }
        }

        public static void validateEmail(String email) {
            if (email == null) {
                throw new UserRegisterException("Register failed, empty email");
            }
            if (email.length() > 100 || !email.contains("@")) {
                throw new UserRegisterException("Register failed, invalid email");
            }
        }

        public static void validateNickname(String nickname) {
            if (nickname == null) {
                throw new UserRegisterException("Register failed, empty nickname");
            }
            if (nickname.length() < 3 || nickname.length() > 50) {
                throw new UserRegisterException("Register failed, invalid nickname");
            }
        }
    }
}
