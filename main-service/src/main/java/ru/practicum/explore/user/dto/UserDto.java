package ru.practicum.explore.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Integer id;
    @NotBlank(message = "Имя пользоваетля не может быть пустым")
    String name;
    @Email(message = "email не соответствует формату")
    @NotBlank(message = "email не указан")
    String email;
}
