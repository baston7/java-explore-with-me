package ru.practicum.explore.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.UserNotFoundException;
import ru.practicum.explore.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {
    private final UserRepository userRepository;

    public List<User> getUsersByIds(List<Integer> ids, int page, int size) {
        List<User> users = userRepository.findUsersByIdIn(ids, PageRequest.of(page, size));
        log.info("Запрос на получение пользователей обработан");
        return users;
    }

    public List<User> getUsers(int page, int size) {
        List<User> users = userRepository.findAll(PageRequest.of(page, size)).stream()
                .collect(Collectors.toList());
        log.info("Запрос на получение пользователей обработан");
        return users;
    }

    public User saveUser(User user) {
        User newUser = userRepository.save(user);
        log.info("Пользователь с id={} сохранен", newUser.getId());
        return newUser;
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }
}
