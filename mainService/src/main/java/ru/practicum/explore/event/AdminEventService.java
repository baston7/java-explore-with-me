package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;

    public Event publishEventById(Integer id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("net takogo eventa"));
        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new RuntimeException("дата начала события должна быть не ранее чем за час от даты публикации.");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new RuntimeException("событие должно быть в состоянии ожидания публикации");
        }
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
        List<State> valueStates = null;
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (states != null) {
            valueStates = states.stream().map(State::valueOf).collect(Collectors.toList());
        }
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        if (users == null && states == null && categories == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAll(PageRequest.of(page, size)).toList();
        } else if (users != null && states == null && categories == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllByInitiatorIdIn(users, PageRequest.of(page, size));
        } else if (users != null && states != null && categories == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllByInitiatorIdInAndStateIn(users, valueStates, PageRequest.of(page, size));
        } else if (users != null && states != null && categories != null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdIn(users, valueStates, categories,
                    PageRequest.of(page, size));
        } else if (users != null && states != null && categories != null && rangeStart != null && rangeEnd == null) {
            events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfter(users,
                    valueStates, categories, start,
                    PageRequest.of(page, size));
        } else if (users != null && states != null && categories != null && rangeStart != null && rangeEnd != null) {
            events = eventRepository
                    .findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(users,
                            valueStates, categories, start, end,
                            PageRequest.of(page, size));
        } else if (users == null && states != null && categories == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllByStateIn(valueStates, PageRequest.of(page, size));
        } else if (users == null && states != null && categories != null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllByStateInAndCategoryIdIn(valueStates, categories, PageRequest.of(page, size));
        } else if (users == null && states != null && categories != null && rangeStart != null && rangeEnd == null) {
            events = eventRepository.findAllByStateInAndCategoryIdInAndEventDateIsAfter(valueStates, categories, start,
                    PageRequest.of(page, size));
        } else if (users == null && states != null && categories != null && rangeStart != null && rangeEnd != null) {
            events = eventRepository.findAllByAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore
                    (valueStates, categories, start, end,
                            PageRequest.of(page, size));
        } else if (users == null && states == null && categories != null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllByCategoryIdIn(categories, PageRequest.of(page, size));
        } else if (users == null && states == null && categories != null && rangeStart != null && rangeEnd == null) {
            events = eventRepository.findAllByCategoryIdInAndEventDateIsAfter(categories, start, PageRequest.of(page,
                    size));
        } else if (users == null && states == null && categories != null && rangeStart != null && rangeEnd != null) {
            events = eventRepository.findAllByCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(categories, start, end,
                    PageRequest.of(page, size));
        } else if (users == null && states == null && categories == null && rangeStart != null && rangeEnd == null) {
            events = eventRepository.findAllByEventDateIsAfter(start, PageRequest.of(page, size));
        } else if (users == null && states == null && categories == null && rangeStart != null && rangeEnd != null) {
            events = eventRepository.findAllByEventDateIsAfterAndEventDateIsBefore(start, end, PageRequest.of(page, size));
        } else if (users == null && states == null && categories == null && rangeStart == null && rangeEnd != null) {
            events = eventRepository.findAllByEventDateIsBefore(end, PageRequest.of(page, size));
        } else if (users != null && states == null && categories != null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllByInitiatorIdInAndCategoryIdIn(users, categories, PageRequest.of(page, size));
        } else if (users != null && states == null && categories == null && rangeStart != null && rangeEnd == null) {
            events = eventRepository.findAllByInitiatorIdInAndEventDateIsAfter(users, start, PageRequest.of(page, size));
        } else if (users != null && states == null && categories == null && rangeStart == null && rangeEnd != null) {
            events = eventRepository.findAllByInitiatorIdInAndEventDateIsBefore(users, end, PageRequest.of(page, size));
        } else if (users != null && states != null && categories == null && rangeStart != null && rangeEnd == null) {
            events = eventRepository.findAllByInitiatorIdInAndStateInAndEventDateIsAfter(users, valueStates, start,
                    PageRequest.of(page, size));
        } else if (users != null && states != null && categories == null && rangeStart == null && rangeEnd != null) {
            events = eventRepository.findAllByInitiatorIdInAndStateInAndEventDateIsBefore(users, valueStates, end,
                    PageRequest.of(page, size));
        } else if (users == null && states != null && categories == null && rangeStart != null && rangeEnd == null) {
            events = eventRepository.findAllByStateInAndEventDateIsAfter(valueStates, start, PageRequest.of(page, size));
        } else if (users == null && states != null && categories == null && rangeStart == null && rangeEnd != null) {
            events = eventRepository.findAllByAndStateInAndEventDateIsBefore(valueStates, end, PageRequest.of(page,
                    size));
        } else if (users == null && states != null && categories == null && rangeStart != null && rangeEnd != null) {
            events = eventRepository.findAllByAndStateInAndEventDateIsAfterAndEventDateIsBefore(valueStates, start,
                    end, PageRequest.of(page, size));
        } else {
            events = eventRepository.findAllByCategoryIdInAndEventDateIsBefore(categories, end, PageRequest.of(page, size));
        }
        if (events.isEmpty()) {
            throw new RuntimeException("не найдено ивентов");
        } else {
            return events;
        }
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
        if (updatingEvent.getEventDate() != null) {
            updatingEvent.setEventDate(adminEvent.getEventDate());
        }
        if (updatingEvent.getLocation() != null) {
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
