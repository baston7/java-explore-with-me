package ru.practicum.explore.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.CommentNotFoundException;
import ru.practicum.explore.exception.ForbiddenException;
import ru.practicum.explore.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentService {
    private final CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        Comment newComment = commentRepository.save(comment);
        log.info("Коментарий с id={} добавлен", newComment.getId());
        return newComment;
    }

    public Comment updateComment(Comment updatingComment, User user, String text) {
        CommentState state = updatingComment.getState();
        if (state.equals(CommentState.PUBLISHED) || state.equals(CommentState.PUBLISHED_WITH_EDITS_ADMIN)) {
            throw new ForbiddenException("Нельзя изменить уже опубликованный комментарий");
        }
        if (user.getId() != updatingComment.getAuthor().getId()) {
            throw new ForbiddenException("Нельзя изменить не свой комментарий");
        }
        updatingComment.setText(text);
        updatingComment.setState(CommentState.PENDING);
        log.info("Коментарий с id={} измененен и отправлен на модерацию ", updatingComment.getId());
        return commentRepository.save(updatingComment);
    }

    public Comment cancelComment(Comment cancelComment, User user) {
        CommentState state = cancelComment.getState();
        if (state.equals(CommentState.PUBLISHED) || state.equals(CommentState.PUBLISHED_WITH_EDITS_ADMIN)) {
            throw new ForbiddenException("Нельзя отменить уже опубликованный комментарий");
        }
        if (state.equals(CommentState.CANCELED)) {
            throw new ForbiddenException("Нельзя отменить уже отмененный комментарий");
        }
        if (user.getId() != cancelComment.getAuthor().getId()) {
            throw new ForbiddenException("Нельзя отменить не свой комментарий");
        }
        cancelComment.setState(CommentState.CANCELED);
        log.info("Коментарий с id={} отменен ", cancelComment.getId());
        return commentRepository.save(cancelComment);
    }

    public List<Comment> getUserComments(Integer userID, int page, int size) {
        List<Comment> comments = commentRepository.findAllByAuthorId(userID, PageRequest.of(page,size));
        if (comments.isEmpty()) {
            throw new CommentNotFoundException("Не найдено комментариев у пользователя");
        }
        return comments;
    }
    public Comment getCommentByIdAndUserId(Integer commentId,Integer userId) {
        return commentRepository.findByIdAndAuthorId(commentId,userId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
    }
}
