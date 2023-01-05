package ru.practicum.explore.event.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.event.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    String annotation;
    @NotNull
    Integer category;
    @NotBlank
    String description;
    @NotBlank
    String eventDate;
    @NotNull
    Location location;
    @NotNull
    boolean paid;
    @Positive
    Integer participantLimit;
    @NotNull
    boolean requestModeration;
    @NotBlank
    String title;
}
