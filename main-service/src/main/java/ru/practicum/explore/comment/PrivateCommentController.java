package ru.practicum.explore.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.event.PrivateEventService;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.user.AdminUserService;
import ru.practicum.explore.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class PrivateCommentController {
    private final PrivateCommentService privateCommentService;
    private final PrivateEventService privateEventService;
    private final AdminUserService adminUserService;

    @PostMapping
    public CommentDto addComment(@PathVariable(name = "userId") Integer userId,
                                 @RequestParam Integer eventId,
                                 @Valid @RequestBody NewOrUpdateCommentDto newCommentDto) {
        log.info("Получен приватный запрос от пользователя с id = {} на добавление комментария к событию" +
                "с id = {}", userId, eventId);
        User user = adminUserService.getUserById(userId);
        Event event = privateEventService.getEventById(eventId);
        Comment comment = CommentMapper.toComment(newCommentDto, user, event);
        return CommentMapper.toCommentDto(privateCommentService.addComment(comment));
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable(name = "userId") Integer userId,
                                    @PathVariable(name = "commentId") Integer commentId,
                                    @Valid @RequestBody NewOrUpdateCommentDto updateCommentDto) {
        log.info("Получен приватный запрос от пользователя с id = {} на изменение комментария с id= {}",
                userId, commentId);
        User user = adminUserService.getUserById(userId);
        Comment updatingComment = privateCommentService.getCommentById(commentId);
        String text = updateCommentDto.getText();
        return CommentMapper.toCommentDto(privateCommentService.updateComment(updatingComment, user, text));
    }

    @PatchMapping("/{commentId}/cancel")
    public CommentDto cancelComment(@PathVariable(name = "userId") Integer userId,
                                    @PathVariable(name = "commentId") Integer commentId) {
        log.info("Получен приватный запрос от пользователя с id = {} на отмену комментария с id= {}",
                userId, commentId);
        User user = adminUserService.getUserById(userId);
        Comment cancelComment = privateCommentService.getCommentById(commentId);
        return CommentMapper.toCommentDto(privateCommentService.cancelComment(cancelComment, user));
    }

    @GetMapping
    public List<CommentDto> getUserComments(@PathVariable(name = "userId") Integer userId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен приватный запрос от пользователя с id = {} на получение всех своих комментариев",
                userId);
        adminUserService.getUserById(userId);
        return privateCommentService.getUserComments(userId, from / size, size).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{commentId}")
    public CommentDto getUserCommentById(@PathVariable(name = "userId") Integer userId,
                                         @PathVariable(name = "commentId") Integer commentId) {
        log.info("Получен приватный запрос от пользователя с id = {} на получение комментария с с id = {}",
                userId, commentId);
        adminUserService.getUserById(userId);
        return CommentMapper.toCommentDto(privateCommentService.getCommentByIdAndUserId(commentId, userId));
    }
}
