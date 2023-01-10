package ru.practicum.explore.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.user.dto.UserShortDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentShortDto {
    Integer id;
    String text;
    UserShortDto author;
    EventShortDto event;
    String state;
    String created;
    String published;
}
