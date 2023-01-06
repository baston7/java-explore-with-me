package ru.practicum.explore.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class CommentMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto=new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorId(comment.getAuthor().getId());
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setCreated(comment.getCreated().format(formatter));
        commentDto.setPublished(comment.getPublished() != null ? comment.getPublished().format(formatter) : null);
        commentDto.setState(comment.getState());
        commentDto.setRejectReason(comment.getRejectReason());
        return commentDto;
    }

    public static Comment toComment(NewOrUpdateCommentDto newCommentDto, User author, Event event) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        comment.setState(CommentState.PENDING);
        return comment;
    }
}
