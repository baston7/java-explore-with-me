package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore.event.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    private String annotation;
    @NotNull
    private Integer category;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private Location location;
    @NotNull
    private boolean paid;
    @Positive
    private Integer participantLimit;
    @NotNull
    private boolean requestModeration;
    @NotBlank
    private String title;
}
