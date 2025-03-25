package com.sb02.blogdemo.core.posting.entity;

import com.sb02.blogdemo.core.posting.exception.InvalidPostError;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Post implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String title;
    private String content;
    private String authorId;
    private List<String> tags;
    private Instant createdAt;
    private Instant updatedAt;

    private Post(
            String title,
            String content,
            String authorId,
            List<String> tags
    ) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.tags = tags;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static Post create(
            String title,
            String content,
            String authorId,
            List<String> tags
    ) {
        Validator.validate(title, content, tags);
        return new Post(title, content, authorId, tags);
    }

    public void update(String title, String content, List<String> tags) {
        if (title != null) {
            Validator.validateTitle(title);
            this.title = title;
        }
        if (content != null) {
            Validator.validateContent(content);
            this.content = content;
        }
        if (tags != null) {
            Validator.validateTags(tags);
            this.tags = tags;
        }
        this.updatedAt = Instant.now();
    }

    private static class Validator {
        public static void validate(String title, String content, List<String> tags) {
            validateTitle(title);
            validateContent(content);
            validateTags(tags);
        }

        public static void validateTitle(String title) {
            if (title == null || title.isBlank()) {
                throw new InvalidPostError("Title must not be empty");
            }
        }

        public static void validateContent(String content) {
            if (content == null || content.isBlank()) {
                throw new InvalidPostError("Content must not be empty");
            }
            if (content.length() < 2 || content.length() > 1000) {
                throw new InvalidPostError("Content must be between 2 and 1000 characters");
            }
        }

        public static void validateTags(List<String> tags) {
            if (tags == null || tags.isEmpty()) {
                throw new InvalidPostError("Tags must not be empty");
            }

            for (String tag : tags) {
                if (tag == null || tag.isBlank()) {
                    throw new InvalidPostError("Tag must not be empty");
                }
                // 영문만 허용, 띄어쓰기 불가
                if (!tag.matches("^[a-zA-Z]+$")) {
                    throw new InvalidPostError("Tag must contain only alphabets: " + tag);
                }
            }
        }
    }
}
