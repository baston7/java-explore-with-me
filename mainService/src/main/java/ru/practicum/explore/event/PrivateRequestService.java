package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateRequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    public Request saveRequest(Request request) {
        Event event = request.getEvent();
        if ((event.getParticipantLimit() == event.getConfirmedRequests()) && event.getParticipantLimit() != 0) {
            throw new RuntimeException("Достигнут лимит участников мероприятия");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RuntimeException("Нельзя  учавствовать в неопубликованном событии");
        }
        if (!requestRepository
                .findAllByRequesterIdAndEvent_IdOrEvent_Initiator_IdAndEvent_Id(request.getRequester().getId(),
                        event.getId(), request.getRequester().getId(), event.getId()).isEmpty()) {
            throw new RuntimeException("Нельзя создать повторный запрос или учавствовать в своем событии");
        }
        if(!request.getEvent().isRequestModeration()){
            event.setConfirmedRequests(event.getConfirmedRequests()+1);
            request.setStatus(State.CONFIRMED);
            eventRepository.save(event);
        }

        return requestRepository.save(request);
    }
    public Request cancelRequest(int requestId, int requesterId) {
        Request request=requestRepository.findById(requestId).orElseThrow(()->new RuntimeException("Не найден запрос на участие"));
        if (request.getRequester().getId()!=requesterId){
            throw new RuntimeException("Нельзя отменить не свою заявку");
        }
        if (request.getStatus().equals(State.CANCELED)){
            throw new RuntimeException("Нельзя отменить уже отмененное событие");
        }
        if(request.getStatus().equals(State.CONFIRMED)){
            Event event=request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests()-1);
            eventRepository.save(event);
        }
        request.setStatus(State.CANCELED);
        return requestRepository.save(request);
    }
    public List<Request> getUserRequests(int requesterId) {
        List<Request> requests=requestRepository.findAllByRequesterId(requesterId);
        if(requests.isEmpty()){
            throw new RuntimeException("Нет заявок у пользователя");
        }
        return requests;
    }
}
