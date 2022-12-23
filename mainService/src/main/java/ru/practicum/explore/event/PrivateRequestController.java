package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.user.AdminUserService;
import ru.practicum.explore.user.model.User;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class PrivateRequestController {
    private final CategoryService categoryService;
    private final AdminUserService adminUserService;
    private final PrivateEventService privateEventService;
    private final PrivateRequestService privateRequestService;

    @PostMapping
    public EventFullDto addEvent(@PathVariable Integer userId, @Valid @RequestBody NewEventDto newEventDto) {
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        User initiator = adminUserService.getUserById(userId);
        Event event = EventMapper.toEventFromNewEventDto(newEventDto, category, initiator);
        return EventMapper.toEventFullDto(privateEventService.saveEvent(event));
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable Integer userId, @RequestBody UpdateEventRequestDto updateEventRequestDto) {
        Category category = categoryService.getCategoryById(updateEventRequestDto.getCategory());
        User initiator = adminUserService.getUserById(userId);
        Event event = EventMapper.toEventFromUpdateEventDto(updateEventRequestDto, category, initiator);
        return EventMapper.toEventFullDto(privateEventService.updateEvent(event));
    }
}
