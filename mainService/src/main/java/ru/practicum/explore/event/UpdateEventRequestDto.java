package ru.practicum.explore.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class UpdateEventRequestDto {
    @NotNull
    private Integer eventId;
    private String annotation;
    private int category;
    private String description;
    private String eventDate;
    private boolean paid;
    private int participantLimit;
    private String title;
}
