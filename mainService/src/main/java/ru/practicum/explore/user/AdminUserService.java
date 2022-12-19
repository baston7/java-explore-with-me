package ru.practicum.explore.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.UserNotFoundException;
import ru.practicum.explore.user.model.User;

import java.awt.print.Pageable;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;

    public List<User> getUsers(List<Integer>ids, int page,int size) {
        return adminUserRepository.findUsersById(ids, PageRequest.of(page, size));
    }
}
