package ru.practicum.explore.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.comment.dto.CommentShortDto;
import ru.practicum.explore.comment.model.Comment;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/events/{id}/comments")
@RequiredArgsConstructor
@Validated
public class PublicCommentController {

    private final PrivateCommentService privateCommentService;

    @GetMapping
    public List<CommentShortDto> findByEvent(@PathVariable(name = "id") Integer eventId,
                                             @RequestParam(required = false) String rangeStartPublish,
                                             @RequestParam(required = false) String rangeEndPublish,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен публичный запрос на просмотр всех комментариев, где eventId= {}", eventId);
        List<Comment> comments = privateCommentService.getCommentsByEventIdWithsParams(eventId, rangeStartPublish,
                rangeEndPublish, from / size, size);
        return comments.stream().
                map(CommentMapper::toCommentShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{commentId}")
    public CommentShortDto findById(@PathVariable(name = "id") Integer eventId,
                                    @PathVariable(name = "commentId") Integer commentId) {
        log.info("Получен публичный запрос на просмотр комментария с id= {} ", commentId);
        return CommentMapper.toCommentShortDto(privateCommentService.getPublishCommentById(commentId, eventId));
    }
}
