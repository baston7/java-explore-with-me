package ru.practicum.explore.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore.user.dto.UserShortDto;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private int id;
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private int views;
}

