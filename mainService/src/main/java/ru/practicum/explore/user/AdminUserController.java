package ru.practicum.explore.user;

import com.sun.jdi.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.model.User;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Positive @RequestParam(defaultValue = "10") int size) {
        if (ids == null) {
            log.info("Получен запрос на поиск пользователей без указания ids");
            return adminUserService.getUsers(from / size, size).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            log.info("Получен запрос на поиск пользователей c ids={}",ids);
            return adminUserService.getUsersByIds(ids, from / size, size).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        log.info("Получен запрос на сохранение пользователя: {}",userDto.getName());
        User savingUser = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(adminUserService.saveUser(savingUser));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Получен запрос на удаление пользователя c id={}",userId);
        adminUserService.deleteUser(userId);
    }
}