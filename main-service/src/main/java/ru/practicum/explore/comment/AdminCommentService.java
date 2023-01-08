package ru.practicum.explore.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.comment.model.Comment;
import ru.practicum.explore.exception.CommentNotFoundException;
import ru.practicum.explore.exception.ForbiddenException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCommentService {
    private final CommentRepository commentRepository;

    public Comment publishComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
        CommentState state = comment.getState();
        if (!state.equals(CommentState.PENDING)) {
            throw new ForbiddenException("Нельзя опубликовать отмененный/опубликованный/отклоненный комментарий");
        }
        comment.setPublished(LocalDateTime.now());
        comment.setState(CommentState.PUBLISHED);
        log.info("Коментарий с id={} опубликован", comment.getId());
        return commentRepository.save(comment);
    }

    public Comment rejectComment(Integer commentId, String reason) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
        CommentState state = comment.getState();
        if (!state.equals(CommentState.PENDING)) {
            throw new ForbiddenException("Нельзя отклонить отмененный/опубликованный/отклоненный комментарий");
        }
        comment.setRejectReason(reason);
        comment.setState(CommentState.REJECTED);
        log.info("Коментарий с id={} отклонен", comment.getId());
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment updatingComment, String text) {
        CommentState state = updatingComment.getState();
        if (!state.equals(CommentState.PENDING)) {
            throw new ForbiddenException("Нельзя изменить отмененный/опубликованный/отклоненный комментарий");
        }
        updatingComment.setText(text);
        updatingComment.setPublished(LocalDateTime.now());
        updatingComment.setState(CommentState.PUBLISHED_WITH_EDITS_ADMIN);
        log.info("Коментарий с id={} измененен и отправлен на модерацию ", updatingComment.getId());
        return commentRepository.save(updatingComment);
    }

    public List<Comment> getCommentsWithConditions(List<Integer> users, List<String> states, List<Integer> events,
                                                   String rangeStart, String rangeEnd, int page, int size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        boolean isUsers = false;
        boolean isEvents = false;
        LocalDateTime start = LocalDateTime.now().minusYears(300);
        LocalDateTime end = LocalDateTime.now().plusYears(300);
        List<CommentState> valueStates = List.of(CommentState.PENDING,
                CommentState.CANCELED, CommentState.PUBLISHED_WITH_EDITS_ADMIN,
                CommentState.PUBLISHED, CommentState.REJECTED);
        if (users != null) {
            isUsers = true;
        }
        if (events != null) {
            isEvents = true;
        }
        if (states != null) {
            valueStates = states.stream().map(CommentState::valueOf).collect(Collectors.toList());
        }
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        List<Comment> comments = commentRepository.findCommentsWithConditions(users, isUsers, events, isEvents, valueStates,
                start, end, PageRequest.of(page, size));
        if (comments.isEmpty()) {
            throw new CommentNotFoundException("Не найдены комментарии с указанными параметрами");
        }
        return comments;
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
    }
}
