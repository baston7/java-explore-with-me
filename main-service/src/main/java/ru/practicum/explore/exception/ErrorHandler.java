package ru.practicum.explore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUserValidationException(final ValidationException exception, WebRequest request) {
        log.error("Ошибка валидации");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason(request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onMethodArgumentNotValidException(final MethodArgumentNotValidException exception, WebRequest request) {
        log.error("Ошибка запроса");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Нарушение ограничений " + request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onConstraintViolationException(final ConstraintViolationException exception, WebRequest request) {
        log.error("Ошибка запроса");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Нарушение целостности SQL данных " + request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final UserNotFoundException exception, WebRequest request) {
        log.error("Ошибка в поиске пользователя. Пользователь не найден");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Пользователи не найдены " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCommentNotFoundException(final CommentNotFoundException exception, WebRequest request) {
        log.error("Ошибка в поиске комментария. Коментарий не найден");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Комментарии не найдены " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventNotFoundException(final EventNotFoundException exception, WebRequest request) {
        log.error("Ошибка в поиске события. Событие не найдено");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Событие не найдено " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFoundException(final CompilationNotFoundException exception,
                                                       WebRequest request) {
        log.error("Ошибка в поиске подборки. Подборка не найдена");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Подборка не найдена " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleRequestNotFoundException(final RequestNotFoundException exception, WebRequest request) {
        log.error("Ошибка в поиске запроса. Запрос не найден");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Запрос не найден " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFoundException(final CategoryNotFoundException exception, WebRequest request) {
        log.error("Ошибка в поиске категории. Категория не найдена");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Категория не найдена " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException exception, WebRequest request) {
        log.error(exception.getMessage());
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Ошибка по адресу- " + request.getDescription(false))
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException exception,
                                                          WebRequest request) {
        log.error("Ошибка в SQL");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Нарушение целостности SQL данных " + request.getDescription(false))
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiError handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception,
                                                      WebRequest request) {
        log.info(exception.getMessage());
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason(request.getDescription(false))
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleRuntimeException(final RuntimeException exception, WebRequest request) {
        log.error("Непредвиденная ошибка");
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .message(exception.getLocalizedMessage())
                .reason("Непредвиденная ошибка " + request.getDescription(false))
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
