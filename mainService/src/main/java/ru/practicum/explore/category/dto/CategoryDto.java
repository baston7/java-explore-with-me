package ru.practicum.explore.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class CategoryDto {
    private int id;
    @NotBlank(message = "Имя категории не может быть пустым ")

    private String name;
}
