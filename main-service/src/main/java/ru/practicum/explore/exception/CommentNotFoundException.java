package ru.practicum.explore.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(final String message) {
        super(message);
    }
}