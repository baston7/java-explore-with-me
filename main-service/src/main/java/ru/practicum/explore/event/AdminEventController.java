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
import ru.practicum.explore.category.CategoryService;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.model.Event;

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
    public EventFullDto publishEvent(@PathVariable(name = "eventId") Integer eventId) {
        log.info("Получен администраторский запрос на публикацию события с id= {} ", eventId);
        Event event = adminEventService.publishEventById(eventId);
        log.info("Событие успешно опубликовано ");
        return EventMapper.toEventFullDto(event);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto cancelEvent(@PathVariable(name = "eventId") Integer eventId) {
        log.info("Получен администраторский запрос на отклонение события с id= {} ", eventId);
        Event event = adminEventService.cancelEventById(eventId);
        log.info("Событие успешно отклонено");
        return EventMapper.toEventFullDto(event);
    }

    @PutMapping("/{eventId}")
    public EventFullDto editingEvent(@PathVariable(name = "eventId") Integer eventId,
                                     @RequestBody AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        log.info("Получен администраторский запрос на изменение события с id= {} ", eventId);
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
        log.info("Получен администраторский запрос на поиск событиий со следующими параметрами:" +
                        " users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        List<Event> events = adminEventService.getEventsWithConditions(users, states, categories,
                rangeStart, rangeEnd, from / size, size);
        log.info("Запрос успешно обработан");
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }
}
