package ru.practicum.explore.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.model.User;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

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
            return null;
        } else {
            return  adminUserService.getUsers(ids,from / size, size).stream()
                    .map(user -> UserMapper.toUserDto(user))
                    .collect(Collectors.toList());
        }
    }
}
