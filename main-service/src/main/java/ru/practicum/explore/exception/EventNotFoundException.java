package ru.practicum.explore.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(final String message) {
        super(message);
    }
}
