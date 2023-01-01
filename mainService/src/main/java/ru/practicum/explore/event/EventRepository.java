package ru.practicum.explore.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByInitiatorId(Integer id, PageRequest pageRequest);

    Optional<Event> findByIdAndState(Integer id, State state);

    Optional<Event> findFirstByCategoryId(Integer id);

    Optional<Event> findEventByIdAndInitiatorId(Integer eventId, Integer userId);

    List<Event> findAllByIdIn(Iterable<Integer> eventsId);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore
            (Iterable<Integer> ids, List<State> states, Iterable<Integer> categories, LocalDateTime rangeStart,
             LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("select e from Event e Where (((?1 is null) or (lower(e.annotation) like CONCAT('%',?1,'%'))) or (((?1 is null) or lower( e.description) like CONCAT('%',?1,'%'))))And (?6=false or (e.category.id in ?2))and (?3 is null or e.paid=?3)and (e.eventDate between ?4 and ?5) and (e.participantLimit=0 or e.participantLimit>e.confirmedRequests) and e.state='PUBLISHED'")
    List<Event> findPublicEvents(String text, List<Integer> categoriesIds, Boolean paid, LocalDateTime start, LocalDateTime end, Boolean categories, Sort sort, PageRequest pageRequest);

    @Query("select e from Event e Where (((?1 is null) or (lower(e.annotation) like CONCAT('%',?1,'%'))) or (((?1 is null) or lower( e.description) like CONCAT('%',?1,'%'))))And (?6=false or (e.category.id in ?2))and (?3 is null or e.paid=?3)and (e.eventDate between ?4 and ?5) and (e.state='PUBLISHED')")
    List<Event> findEventsWithParamsWithoutLimit(String text, List<Integer> categoriesIds, Boolean paid, LocalDateTime start, LocalDateTime end, boolean categories, Sort sort, PageRequest pageRequest);

}
