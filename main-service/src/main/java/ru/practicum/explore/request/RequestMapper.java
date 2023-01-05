package ru.practicum.explore.request;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.event.State;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class RequestMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toParticipationRequestDtoFromIds(Integer userId, Integer eventId) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setRequester(userId);
        participationRequestDto.setEvent(eventId);
        participationRequestDto.setCreated(LocalDateTime.now().format(formatter));
        participationRequestDto.setStatus(State.PENDING.toString());
        return participationRequestDto;
    }

    public static ParticipationRequestDto toParticipationRequestDtoFromRequest(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setCreated(request.getCreated().format(formatter));
        participationRequestDto.setId(request.getId());
        participationRequestDto.setStatus(request.getStatus().toString());
        return participationRequestDto;
    }

    public static Request toRequest(ParticipationRequestDto participationRequestDto, User requester, Event event) {
        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.parse(participationRequestDto.getCreated(), formatter));
        request.setStatus(State.valueOf(participationRequestDto.getStatus()));
        return request;
    }
}
