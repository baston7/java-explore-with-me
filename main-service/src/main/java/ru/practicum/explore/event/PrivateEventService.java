package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.exception.EventNotFoundException;
import ru.practicum.explore.exception.ForbiddenException;
import ru.practicum.explore.exception.RequestNotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.request.RequestRepository;
import ru.practicum.explore.request.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public Event saveEvent(Event event) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Дата и время на которые намечено событие не может быть раньше," +
                    " чем через два часа от текущего момента");
        }
        Event newEvent = eventRepository.save(event);
        log.info("Событие с id={} добавлено", newEvent.getId());
        return newEvent;
    }

    public Event updateEvent(Event newEvent) {
        Event updatingEvent = eventRepository.findById(newEvent.getId())
                .orElseThrow(() -> new EventNotFoundException("Не найдено событие на обновление"));
        if (newEvent.getInitiator().getId() != updatingEvent.getInitiator().getId()) {
            throw new ForbiddenException("Только создатель может обновлять мероприятие");
        }
        if (updatingEvent.getState() == State.PUBLISHED) {
            throw new ForbiddenException("Нельзя изменить уже опубликованное администратором событие");
        }
        if (newEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Дата и время на которые намечено событие не может быть раньше," +
                    " чем через два часа от текущего момента");
        }
        if (updatingEvent.getState() == State.CANCELED) {
            updatingEvent.setState(State.PENDING);
        }
        setNewFieldsForUpdateEvent(newEvent, updatingEvent);
        log.info("Событие с id={} обновлено", newEvent.getId());
        return eventRepository.save(updatingEvent);
    }

    public List<Event> getEventsByInitiatorId(Integer initiatorId, int page, int size) {
        List<Event> userEvents = new ArrayList<>(eventRepository.findAllByInitiatorId(initiatorId,
                PageRequest.of(page, size)));
        if (userEvents.isEmpty()) {
            throw new EventNotFoundException("Нет событий у пользователя");
        }
        log.info("Запрос обработан");
        return userEvents;
    }

    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
    }

    public Event getEventByEventIdAndInitiatorId(Integer eventId, Integer initiatorId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new EventNotFoundException("Не найдено событие пользователя"));
    }

    public Event cancelEvent(Event event) {
        if (event.getState().equals(State.CANCELED)) {
            throw new ForbiddenException("Нельзя отменить уже отмененное событие");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Нельзя отменить уже опубликованное событие");
        }
        event.setState(State.CANCELED);
        log.info("Событие с id={} отменено", event.getId());
        return eventRepository.save(event);
    }

    public List<Request> getRequestsFromUserEvent(int eventId, int initiatorId) {
        List<Request> requests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, initiatorId);
        if (requests.isEmpty()) {
            throw new RequestNotFoundException("нет заявок на событие пользователя");
        }
        log.info("Запрос на получение заявок обработан");
        return requests;
    }

    public Request confirmRequestToUserEvent(int eventId, int userId, int reqId) {
        Optional<Request> request = requestRepository
                .findByIdAndEventIdAndEventInitiatorIdAndEventRequestModeration(reqId, eventId, userId, true);
        if (request.isEmpty()) {
            throw new RequestNotFoundException("Нет заявки на подтверждение");

        } else if (request.get().getEvent().getParticipantLimit() == 0) {
            throw new ForbiddenException("На данное событие нельзя подтвержать заявки");
        } else {
            Request valueRequest = request.get();
            Event event = request.get().getEvent();
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                throw new ForbiddenException("Нельзя подтвердить заявку," +
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
            log.info("Запрос на подтверждение заявки обработан");
            return requestRepository.save(valueRequest);
        }
    }

    public Request rejectRequestToUserEvent(int eventId, int userId, int reqId) {
        Optional<Request> request = requestRepository
                .findByIdAndEventIdAndEventInitiatorIdAndEventRequestModeration(reqId, eventId, userId, true);
        if (request.isEmpty()) {
            throw new RequestNotFoundException("Нет заявки на отклонение");

        } else if (request.get().getEvent().getParticipantLimit() == 0) {
            throw new ForbiddenException("Нельзя отклонять заявки на это событие");
        } else {
            Request valueRequest = request.get();
            valueRequest.setStatus(State.REJECTED);
            log.info("Запрос на отклонение заявки обработан");
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
