package ru.practicum.explore.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.CommentNotFoundException;
import ru.practicum.explore.exception.ForbiddenException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCommentService {
    private final CommentRepository commentRepository;

    public Comment publishComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
        CommentState state=comment.getState();
        if (!state.equals(CommentState.PENDING)) {
            throw new ForbiddenException("Нельзя опубликовать либо отмененный/опубликованный/отклоненный комментарий");
        }
        comment.setPublished(LocalDateTime.now());
        comment.setState(CommentState.PUBLISHED);
        log.info("Коментарий с id={} опубликован", comment.getId());
        return commentRepository.save(comment);
    }
    public Comment rejectComment(Integer commentId,String reason) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
        CommentState state=comment.getState();
        if (!state.equals(CommentState.PENDING)) {
            throw new ForbiddenException("Нельзя отклонить либо отмененный/опубликованный/отклоненный комментарий");
        }
        comment.setRejectReason(reason);
        comment.setState(CommentState.REJECTED);
        log.info("Коментарий с id={} отклонен", comment.getId());
        return commentRepository.save(comment);
    }
}
