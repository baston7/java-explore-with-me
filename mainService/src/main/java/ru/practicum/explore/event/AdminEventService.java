package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.CategoryRepository;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.exception.EventNotFoundException;
import ru.practicum.explore.exception.ForbiddenException;
import ru.practicum.explore.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public Event publishEventById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new ForbiddenException("дата начала события должна быть не ранее чем за час от даты публикации.");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ForbiddenException("событие должно быть в состоянии ожидания публикации");
        }
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PUBLISHED);
        log.info("Событие с id={} опубликовано", id);
        return eventRepository.save(event);
    }

    public Event cancelEventById(Integer id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Событие уже опубликовано.Нельзя отменить событие");
        }
        event.setState(State.CANCELED);
        log.info("Событие с id={} отменено", id);
        return eventRepository.save(event);
    }

    public Event editingEvent(Integer eventId, Event adminEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        setNewFieldsForEditingAdminEvent(adminEvent, event);
        log.info("Событие с id={} отредактировано", eventId);
        return eventRepository.save(event);
    }

    public List<Event> getEventsWithConditions(List<Integer> users, List<String> states, List<Integer> categories,
                                               String rangeStart, String rangeEnd, int page, int size) {
        List<Event> events;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Integer> usersIds;
        List<State> valueStates;
        List<Integer> categoriesIds;
        LocalDateTime start;
        LocalDateTime end;
        if (users != null) {
            usersIds = users;
        } else {
            usersIds = userRepository.findUsersIds();
        }
        if (categories != null) {
            categoriesIds = categories;
        } else {
            categoriesIds = categoryRepository.findCategoriesIds();
        }
        if (states != null) {
            valueStates = states.stream().map(State::valueOf).collect(Collectors.toList());
        } else {
            valueStates = List.of(State.CONFIRMED, State.CANCELED, State.PENDING, State.PUBLISHED, State.REJECTED);
        }
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        } else {
            start = LocalDateTime.now().minusYears(300);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, formatter);
        } else {
            end = LocalDateTime.now().plusYears(300);
        }
        events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(
                usersIds, valueStates, categoriesIds, start, end, PageRequest.of(page, size)
        );
        if (events.isEmpty()) {
            throw new EventNotFoundException("Событий не найдено");
        }
        return events;
    }

    private void setNewFieldsForEditingAdminEvent(Event adminEvent, Event updatingEvent) {

        updatingEvent.setPaid(adminEvent.isPaid());
        updatingEvent.setRequestModeration(adminEvent.isRequestModeration());
        if (adminEvent.getAnnotation() != null) {
            updatingEvent.setAnnotation(adminEvent.getAnnotation());
        }
        if (adminEvent.getCategory() != null) {
            updatingEvent.setCategory(adminEvent.getCategory());
        }
        if (adminEvent.getDescription() != null) {
            updatingEvent.setDescription(adminEvent.getDescription());
        }
        if (adminEvent.getEventDate() != null) {
            updatingEvent.setEventDate(adminEvent.getEventDate());
        }
        if (adminEvent.getLocation() != null) {
            updatingEvent.setLocation(adminEvent.getLocation());
        }
        if (adminEvent.getParticipantLimit() != null) {
            updatingEvent.setParticipantLimit(adminEvent.getParticipantLimit());
        }
        if (adminEvent.getTitle() != null) {
            updatingEvent.setTitle(adminEvent.getTitle());
        }
    }
}
