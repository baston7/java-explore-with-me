package ru.practicum.explore.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(final String message) {
        super(message);
    }
}