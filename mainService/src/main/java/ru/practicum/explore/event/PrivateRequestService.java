package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PrivateRequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    public Request saveRequest(Request request) {
        if(requestRepository.findByRequesterIdAndEventId(request.getRequester().getId(),request.getEvent().getId()).isPresent()){
            throw new RuntimeException("Нельзя создать повторный запрос");
        }
        if(eventRepository.findEventByIdAndInitiatorId(request.getEvent().getId(),request.getRequester().getId()).isPresent()){
            throw new RuntimeException("Нельзя учавствовать в своем событии");
        }
       return requestRepository.save(request);
    }
}
