package ru.practicum.explore.comment;

import javax.validation.constraints.Size;

public class CommentDtoWithAdminMessage {
    int id;
    @Size(min = 1, max = 3000, message = "Комментарий должен содержать от 1 до 3000 символов")
    String text;
    int authorId;
    int eventId;
    String created;
    String reason;
}
