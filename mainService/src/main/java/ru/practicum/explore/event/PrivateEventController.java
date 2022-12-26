package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.user.AdminUserService;
import ru.practicum.explore.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final CategoryService categoryService;
    private final AdminUserService adminUserService;
    private final PrivateEventService privateEventService;

    @PostMapping
    public EventFullDto addEvent(@PathVariable Integer userId, @Valid @RequestBody NewEventDto newEventDto) {
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        User initiator = adminUserService.getUserById(userId);
        Event event = EventMapper.toEventFromNewEventDto(newEventDto, category, initiator);
        return EventMapper.toEventFullDto(privateEventService.saveEvent(event));
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable Integer userId, @RequestBody UpdateEventRequestDto updateEventRequestDto) {
        Category category = null;
        if (updateEventRequestDto.getCategory() != 0) {
            category = categoryService.getCategoryById(updateEventRequestDto.getCategory());
        }
        User initiator = adminUserService.getUserById(userId);
        Event event = EventMapper.toEventFromUpdateEventDto(updateEventRequestDto, category, initiator);
        return EventMapper.toEventFullDto(privateEventService.updateEvent(event));
    }

    @GetMapping
    public List<EventFullDto> getUserEvents(@PathVariable Integer userId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size) {
        adminUserService.getUserById(userId);
        return privateEventService.getEventsByInitiatorId(userId, from / size, size).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Integer userId,
                                     @PathVariable Integer eventId) {
        adminUserService.getUserById(userId);
        return EventMapper.toEventFullDto(privateEventService.getEventByEventIdAndInitiatorId(eventId, userId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId) {
        adminUserService.getUserById(userId);
        Event event = privateEventService.getEventByEventIdAndInitiatorId(eventId, userId);
        return EventMapper.toEventFullDto(privateEventService.cancelEvent(event));
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsFromEvent(@PathVariable Integer userId,
                                                              @PathVariable Integer eventId) {
        adminUserService.getUserById(userId);
        return privateEventService.getRequestsFromUserEvent(eventId, userId).stream()
                .map(RequestMapper::toParticipationRequestDtoFromRequest)
                .collect(Collectors.toList());

    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestToUserEvent(@PathVariable Integer userId,
                                                                   @PathVariable Integer eventId,
                                                                   @PathVariable Integer reqId) {
        adminUserService.getUserById(userId);
        return RequestMapper.toParticipationRequestDtoFromRequest(privateEventService.confirmRequestToUserEvent(eventId, userId,reqId));

    }
    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestToUserEvent(@PathVariable Integer userId,
                                                             @PathVariable Integer eventId,
                                                             @PathVariable Integer reqId) {
        adminUserService.getUserById(userId);
        return RequestMapper.toParticipationRequestDtoFromRequest(privateEventService.rejectRequestToUserEvent(eventId, userId,reqId));

    }
}
