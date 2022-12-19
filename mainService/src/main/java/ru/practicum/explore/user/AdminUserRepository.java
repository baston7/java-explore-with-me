package ru.practicum.explore.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.user.model.User;

import java.util.Arrays;
import java.util.List;

@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {
    List<User> findUsersById(Iterable<Integer>ids, PageRequest pageRequest);
}
