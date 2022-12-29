package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public Event publishEventById(Integer id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("net takogo eventa"));
        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new RuntimeException("дата начала события должна быть не ранее чем за час от даты публикации.");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new RuntimeException("событие должно быть в состоянии ожидания публикации");
        }
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PUBLISHED);
        return eventRepository.save(event);
    }

    public Event cancelEventById(Integer id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("net takogo eventa"));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new RuntimeException("событие уже опубликовано.");
        }
        event.setState(State.CANCELED);
        return eventRepository.save(event);
    }

    public Event editingEvent(Integer eventId, Event adminEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("net takogo eventa"));
        setNewFieldsForEditingAdminEvent(adminEvent, event);
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
        events=eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(
                usersIds,valueStates,categoriesIds,start,end,PageRequest.of(page,size)
        );
        if(events.isEmpty()){
            throw new RuntimeException("ne naideno sobytiy");
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
