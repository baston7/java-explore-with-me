package ru.practicum.explore.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUsersByIdIn(Iterable<Integer> ids, PageRequest pageRequest);

    @Query("select id from User")
    List<Integer> findUsersIds();
}
