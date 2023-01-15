package ru.practicum.explore.comment;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAuthorId(Integer authorId, PageRequest pageRequest);

    Optional<Comment> findByIdAndAuthorId(Integer commentId, Integer authorId);

    Optional<Comment> findByIdAndEventIdAndStateIn(Integer commentId, Integer eventId, List<CommentState> states);

    @Query("select c" +
            " from Comment c" +
            " Where ((?2=false or (c.author.id in ?1)) and (?4=false or (c.event.id in ?3)) and (c.state in ?5)" +
            " and (c.created between ?6 and ?7))")
    List<Comment> findCommentsWithConditions(List<Integer> usersId, boolean isUsers,
                                             List<Integer> eventsId, boolean isEvents,
                                             List<CommentState> states, LocalDateTime start,
                                             LocalDateTime end, PageRequest pageRequest);

    @Query("select c" +
            " from Comment c" +
            " where (c.event.id=?1 and c.state in ?2 and c.published between ?3 and ?4) order by c.created desc ")
    List<Comment> getCommentsByEventIdWithsParams(Integer eventId, List<CommentState> states, LocalDateTime start,
                                                  LocalDateTime end, PageRequest pageRequest);
}
