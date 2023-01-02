package ru.practicum.explore.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.event.PrivateEventService;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.user.AdminUserService;
import ru.practicum.explore.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class PrivateRequestController {
    private final AdminUserService adminUserService;
    private final PrivateEventService privateEventService;
    private final PrivateRequestService privateRequestService;

    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable Integer userId, @RequestParam Integer eventId) {
        log.info("Получен приватный запрос от пользователя с id = {} на участие" +
                " в событии с id = {}", userId, eventId);
        ParticipationRequestDto participationRequestDto = RequestMapper.toParticipationRequestDtoFromIds(userId, eventId);
        User requester = adminUserService.getUserById(userId);
        Event event = privateEventService.getEventById(eventId);
        Request request = RequestMapper.toRequest(participationRequestDto, requester, event);
        return RequestMapper.toParticipationRequestDtoFromRequest(privateRequestService.saveRequest(request));
    }

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Integer userId) {
        log.info("Получен приватный запрос от пользователя с id = {} на просмотр своих заявок", userId);
        return privateRequestService.getUserRequests(userId).stream()
                .map(RequestMapper::toParticipationRequestDtoFromRequest)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        log.info("Получен приватный запрос от пользователя с id = {} на отмену заявки с id= {}", userId, requestId);
        adminUserService.getUserById(userId);
        return RequestMapper.toParticipationRequestDtoFromRequest(privateRequestService.cancelRequest(requestId, userId));
    }
}
