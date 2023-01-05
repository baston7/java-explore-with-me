package ru.practicum.explore.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.event.State;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class CommentMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(),
                comment.getAuthor().getId(), comment.getEvent().getId(), comment.getCreated().format(formatter));
    }

    public static Comment toComment(String text, User author, Event event) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        comment.setState(CommentState.PENDING);
        return comment;
    }
}
