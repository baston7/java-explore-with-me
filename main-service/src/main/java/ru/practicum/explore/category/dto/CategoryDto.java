package ru.practicum.explore.category.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NotNull
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    Integer id;
    @NotBlank(message = "Имя категории не может быть пустым ")
    String name;
}
