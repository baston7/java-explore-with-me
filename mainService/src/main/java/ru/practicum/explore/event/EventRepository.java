package ru.practicum.explore.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByInitiatorId(Integer id, PageRequest pageRequest);
    Optional<Event> findFirstByCategoryId(Integer id);
    Optional<Event> findEventByIdAndInitiatorId(Integer eventId, Integer userId);


    List<Event> findAllByInitiatorIdIn(Iterable<Integer> ids, PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndStateIn(Iterable<Integer> ids, Iterable<State> states, PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdIn(Iterable<Integer> ids, List<State> states,
                                                                Iterable<Integer> categories, PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfter(Iterable<Integer> ids, List<State> states,
                                                                                   Iterable<Integer> categories, LocalDateTime rangeStart,
                                                                                   PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore
            (Iterable<Integer> ids, List<State> states, Iterable<Integer> categories, LocalDateTime rangeStart,
             LocalDateTime rangeEnd, PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndCategoryIdIn(Iterable<Integer> ids,
                                                      Iterable<Integer> categories, PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndEventDateIsAfter(Iterable<Integer> ids, LocalDateTime rangeStart,
                                                          PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndEventDateIsBefore(Iterable<Integer> ids, LocalDateTime rangeEnd,
                                                           PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndStateInAndEventDateIsAfter(Iterable<Integer> ids, List<State> states,
                                                                    LocalDateTime rangeStart,
                                                                    PageRequest pageRequest);

    List<Event> findAllByInitiatorIdInAndStateInAndEventDateIsBefore(Iterable<Integer> ids, List<State> states,
                                                                     LocalDateTime rangeEnd, PageRequest pageRequest);

    //_______________________________________________________________________________________________________
    List<Event> findAllByStateIn(List<State> states, PageRequest pageRequest);

    List<Event> findAllByStateInAndCategoryIdIn(List<State> states,
                                                Iterable<Integer> categories, PageRequest pageRequest);

    List<Event> findAllByStateInAndCategoryIdInAndEventDateIsAfter(List<State> states,
                                                                   Iterable<Integer> categories, LocalDateTime rangeStart,
                                                                   PageRequest pageRequest);

    List<Event> findAllByAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore
            (List<State> states, Iterable<Integer> categories, LocalDateTime rangeStart,
             LocalDateTime rangeEnd, PageRequest pageRequest);

    List<Event> findAllByStateInAndEventDateIsAfter(List<State> states, LocalDateTime rangeStart,
                                                    PageRequest pageRequest);

    List<Event> findAllByAndStateInAndEventDateIsBefore(List<State> states, LocalDateTime rangeEnd,
                                                        PageRequest pageRequest);

    List<Event> findAllByAndStateInAndEventDateIsAfterAndEventDateIsBefore(List<State> states,
                                                                           LocalDateTime rangeStart,
                                                                           LocalDateTime rangeEnd,
                                                                           PageRequest pageRequest);
    //_______________________________________________________________________________________________________


    List<Event> findAllByCategoryIdIn(Iterable<Integer> categories, PageRequest pageRequest);

    List<Event> findAllByCategoryIdInAndEventDateIsAfter(Iterable<Integer> categories, LocalDateTime rangeStart,
                                                         PageRequest pageRequest);

    List<Event> findAllByCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(Iterable<Integer> categories,
                                                                             LocalDateTime rangeStart,
                                                                             LocalDateTime rangeEnd,
                                                                             PageRequest pageRequest);
    List<Event> findAllByCategoryIdInAndEventDateIsBefore(Iterable<Integer> categories,
                                                                             LocalDateTime rangeEnd,
                                                                             PageRequest pageRequest);
//--------------------------------------------------------------------------------------------------------------


    List<Event> findAllByEventDateIsAfter(LocalDateTime rangeStart, PageRequest pageRequest);

    List<Event> findAllByEventDateIsAfterAndEventDateIsBefore(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                              PageRequest pageRequest);
    //______________________________________________________________________________________________________


    List<Event> findAllByEventDateIsBefore(LocalDateTime rangeEnd, PageRequest pageRequest);
}
