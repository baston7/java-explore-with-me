package ru.practicum.explore.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.State;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.exception.ForbiddenException;
import ru.practicum.explore.exception.RequestNotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.request.model.Request;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    public Request saveRequest(Request request) {
        Event event = request.getEvent();
        if ((event.getParticipantLimit() == event.getConfirmedRequests()) && event.getParticipantLimit() != 0) {
            throw new ForbiddenException("Достигнут лимит участников мероприятия");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Нельзя  учавствовать в неопубликованном событии");
        }
        if (!requestRepository
                .findAllByRequesterIdAndEventIdOrEventInitiatorIdAndEventId(request.getRequester().getId(),
                        event.getId(), request.getRequester().getId(), event.getId()).isEmpty()) {
            throw new ValidationException("Нельзя создать повторный запрос или учавствовать в своем событии");
        }
        if (!request.getEvent().isRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            request.setStatus(State.CONFIRMED);
            log.info("Увеличено количество подтвержденных заявок в событии с id={}", request.getId());
            eventRepository.save(event);
        }
        log.info("Запрос на публикацию запроса обработан");
        return requestRepository.save(request);
    }

    public Request cancelRequest(int requestId, int requesterId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Не найден запрос на участие"));
        if (request.getRequester().getId() != requesterId) {
            throw new ForbiddenException("Нельзя отменить не свою заявку");
        }
        if (request.getStatus().equals(State.CANCELED)) {
            throw new ForbiddenException("Нельзя отменить уже отмененное событие");
        }
        if (request.getStatus().equals(State.CONFIRMED)) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            log.info("Уменьшено количество подтвержденных заявок в событии с id={}", request.getId());
            eventRepository.save(event);
        }
        request.setStatus(State.CANCELED);
        log.info("Запрос на отмену запроса обработан");
        return requestRepository.save(request);
    }

    public List<Request> getUserRequests(int requesterId) {
        List<Request> requests = requestRepository.findAllByRequesterId(requesterId);
        if (requests.isEmpty()) {
            throw new RequestNotFoundException("Нет заявок у пользователя");
        }
        log.info("Запрос на получение заявок пользователя обработан");
        return requests;
    }
}
