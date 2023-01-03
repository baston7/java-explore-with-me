package ru.practicum.explore.event.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequestDto {
    @NotNull
    Integer eventId;
    String annotation;
    int category;
    String description;
    String eventDate;
    boolean paid;
    int participantLimit;
    String title;
}
