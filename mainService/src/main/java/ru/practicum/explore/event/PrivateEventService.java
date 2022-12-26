package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

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
        if (event.getState().equals(State.CANCELED)) {
            throw new RuntimeException("нельзя отменить уже отмененное событие");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new RuntimeException("нельзя отменить уже опубликованное событие");
        }
        event.setState(State.CANCELED);
        return eventRepository.save(event);
    }

    public List<Request> getRequestsFromUserEvent(int eventId, int initiatorId) {
        List<Request> requests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, initiatorId);
        if (requests.isEmpty()) {
            throw new RuntimeException("нет заявок на события пользователя или самих событий нет");
        }
        return requests;
    }

    public Request confirmRequestToUserEvent(int eventId, int userId, int reqId) {
        Optional<Request> request = requestRepository
                .findByIdAndEventIdAndEventInitiatorIdAndEventRequestModeration(reqId, eventId, userId, true);
        if (request.isEmpty()) {
            throw new RuntimeException("нет заявки на подтверждение");

        } else if (request.get().getEvent().getParticipantLimit() == 0) {
            throw new RuntimeException("нет заявки на подтверждение");
        } else {
            Request valueRequest = request.get();
            Event event = request.get().getEvent();
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                throw new RuntimeException("нельзя подтвердить заявку," +
                        " если уже достигнут лимит по заявкам на данное событие");
            }

            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                List<Request> allRequests = requestRepository
                        .findAllByEventIdAndEventInitiatorIdAndEventRequestModeration(eventId, userId, true);
                if (!allRequests.isEmpty()) {
                    allRequests.forEach(request1 -> request1.setStatus(State.CANCELED));
                    requestRepository.saveAll(allRequests);
                }
            }
            valueRequest.setStatus(State.CONFIRMED);
            return requestRepository.save(valueRequest);
        }
    }

    public Request rejectRequestToUserEvent(int eventId, int userId, int reqId) {
        Optional<Request> request = requestRepository
                .findByIdAndEventIdAndEventInitiatorIdAndEventRequestModeration(reqId, eventId, userId, true);
        if (request.isEmpty()) {
            throw new RuntimeException("нет заявки на отклонение");

        } else if (request.get().getEvent().getParticipantLimit() == 0) {
            throw new RuntimeException("нет заявки на отклонение");
        } else {
            Request valueRequest = request.get();
            valueRequest.setStatus(State.REJECTED);
            return requestRepository.save(valueRequest);
        }
    }
    public List<Event> getAllByEventsIds(List<Integer> eventsIds) {
        return eventRepository.findAllByIdIn(eventsIds);
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
