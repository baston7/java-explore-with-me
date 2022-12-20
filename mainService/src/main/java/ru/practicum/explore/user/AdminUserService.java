package ru.practicum.explore.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;

    public List<User> getUsersByIds(List<Integer>ids, int page,int size) {
        return adminUserRepository.findUsersByIdIn(ids, PageRequest.of(page, size));
    }
    public List<User> getUsers( int page,int size) {
        return adminUserRepository.findAll(PageRequest.of(page, size)).stream()
                .collect(Collectors.toList());
    }
    public User saveUser(User user) {
        return adminUserRepository.save(user);
    }
    public void deleteUser(Integer userId) {
        adminUserRepository.deleteById(userId);
    }
}
