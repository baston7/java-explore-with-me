package ru.practicum.explore.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    @NotBlank(message = "Имя пользоваетля не может быть пустым")
    private String name;
    @Email(message = "email не соответствует формату")
    @NotBlank(message = "email не указан")
    private String email;
}