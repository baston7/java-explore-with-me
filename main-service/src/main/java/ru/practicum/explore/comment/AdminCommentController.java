package ru.practicum.explore.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor

public class AdminCommentController {
    private final AdminCommentService adminCommentService;

    @PatchMapping("/{commentId}/publish")
    public CommentDto publishComment(@PathVariable(name = "commentId") Integer commentId) {
        log.info("Получен администраторский запрос на публикацию комментария с id= {} ", commentId);
        return CommentMapper.toCommentDto(adminCommentService.publishComment(commentId));
    }

    @PatchMapping("/{commentId}/reject")
    public CommentDto rejectComment(@PathVariable(name = "commentId") Integer commentId,
                                    @RequestParam(defaultValue = "Без указания причины") String reason) {
        log.info("Получен администраторский запрос на отклонение комментария с id= {}. Причина отклонения: {}",
                commentId, reason);
        return CommentMapper.toCommentDto(adminCommentService.rejectComment(commentId,reason));
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable(name = "commentId") Integer commentId,
                                    @RequestBody NewOrUpdateCommentDto newOrUpdateCommentDto) {
        log.info("Получен администраторский запрос на изменение комментария с id = {}.", commentId);
    }

    @GetMapping
    public List<CommentDto> getComments(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Integer> events,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                        @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен администраторский запрос на поиск комментариев со следующими параметрами:" +
                        " users: {}, states: {}, events: {}, rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
                users, states, events, rangeStart, rangeEnd, from, size);
        List<Comment> comments = adminCommentService.getCommentsWithConditions(users, states, events,
                rangeStart, rangeEnd, from / size, size);
        log.info("Запрос успешно обработан");
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
