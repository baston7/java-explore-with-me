package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final AdminEventService adminEventService;
    private final CategoryService categoryService;

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Integer eventId) {
        Event event = adminEventService.publishEventById(eventId);
        return EventMapper.toEventFullDto(event);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto cancelEvent(@PathVariable Integer eventId) {
        Event event = adminEventService.cancelEventById(eventId);
        return EventMapper.toEventFullDto(event);
    }

    @PutMapping("/{eventId}")
    public EventFullDto editingEvent(@PathVariable Integer eventId,
                                     @RequestBody AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        Category category = null;
        if (adminUpdateEventRequestDto.getCategory() != 0) {
            category = categoryService.getCategoryById(adminUpdateEventRequestDto.getCategory());
        }
        Event event = EventMapper.toEventFromAdminUpdateEventRequestDto(adminUpdateEventRequestDto, category);
        return EventMapper.toEventFullDto(adminEventService.editingEvent(eventId, event));
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                        @Positive @RequestParam(defaultValue = "10") int size) {
        List<Event> events = adminEventService.getEventsWithConditions(users, states, categories,
                rangeStart, rangeEnd, from / size, size);
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }
}