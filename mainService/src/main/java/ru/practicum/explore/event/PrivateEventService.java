package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;

    public Event saveEvent(Event event) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new RuntimeException("vrem9 ploxoe");
        }
        return eventRepository.save(event);
    }

    public Event updateEvent(Event newEvent) {
        Event updatingEvent = eventRepository.findById(newEvent.getId()).orElseThrow(() -> new RuntimeException("ne naiden event na obnovlenie"));
        if (newEvent.getInitiator().getId() != updatingEvent.getInitiator().getId()) {
            throw new RuntimeException("ids polzovateley ne sovpadayut");
        }
        if (updatingEvent.getState() == State.PUBLISHED) {
            throw new RuntimeException("nelz9 izmenit opublikovannoe sobytie");
        }
        if (newEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new RuntimeException("vrem9 ploxoe");
        }
        if (updatingEvent.getState() == State.CANCELED) {
            updatingEvent.setState(State.PENDING);
        }
        setNewFieldsForUpdateEvent(newEvent, updatingEvent);
        return eventRepository.save(updatingEvent);
    }

    public List<Event> getEventsByInitiatorId(Integer initiatorId, int page, int size) {
        List<Event> userEvents = new ArrayList<>(eventRepository.findAllByInitiatorId(initiatorId, PageRequest.of(page, size)));
        if (userEvents.isEmpty()) {
            throw new RuntimeException("нет событий у пользователя");
        }
        return userEvents;
    }
    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("ne naiden event"));
    }

    public Event getEventByEventIdAndInitiatorId(Integer eventId, Integer initiatorId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new RuntimeException("ne naiden event polzovatel9"));
    }
    public Event cancelEvent(Event event) {
        if(event.getState().equals(State.CANCELED)){
            throw new RuntimeException("нельзя отменить уже отмененное событие");
        }
        if(event.getState().equals(State.PUBLISHED)){
            throw new RuntimeException("нельзя отменить уже опубликованное событие");
        }
        event.setState(State.CANCELED);
        return eventRepository.save(event);
    }


    private void setNewFieldsForUpdateEvent(Event newEvent, Event updatingEvent) {

        updatingEvent.setPaid(newEvent.isPaid());
        if (newEvent.getAnnotation() != null) {
            updatingEvent.setAnnotation(newEvent.getAnnotation());
        }
        if (newEvent.getCategory() != null) {
            updatingEvent.setCategory(newEvent.getCategory());
        }
        if (newEvent.getDescription() != null) {
            updatingEvent.setDescription(newEvent.getDescription());
        }
        if (newEvent.getEventDate() != null) {
            updatingEvent.setEventDate(newEvent.getEventDate());
        }
        if (newEvent.getParticipantLimit() != null) {
            updatingEvent.setParticipantLimit(newEvent.getParticipantLimit());
        }
        if (newEvent.getTitle() != null) {
            updatingEvent.setTitle(newEvent.getTitle());
        }
    }
}
