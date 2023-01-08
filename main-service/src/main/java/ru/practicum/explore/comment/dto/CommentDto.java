package ru.practicum.explore.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.comment.CommentState;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    int id;
    String text;
    int authorId;
    int eventId;
    CommentState state;
    String published;
    String created;
    String rejectReason;
}
