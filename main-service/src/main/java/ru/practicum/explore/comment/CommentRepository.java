package ru.practicum.explore.comment;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAuthorId(Integer authorId, PageRequest pageRequest);

    Optional<Comment> findByIdAndAuthorId(Integer commentId, Integer authorId);
}
