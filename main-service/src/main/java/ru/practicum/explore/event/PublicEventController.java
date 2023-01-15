package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventShortDto> findAll(HttpServletRequest request, @RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Integer> categories,
                                       @RequestParam(required = false) Boolean paid,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                       @RequestParam(required = false) String sort,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                       @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен публичный запрос на поиск событиий со следующими параметрами:" +
                        " text: {}, categories: {}, paid: {}, rangeStart: {}, rangeEnd: {}, onlyAvailable: {}," +
                        " sort: {}, from: {}, size: {}", text,
                categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return publicEventService.findAll(request, text, categories, paid, rangeStart, rangeEnd, sort, onlyAvailable,
                        PageRequest.of(from / size, size)).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventFullDto findById(HttpServletRequest request, @PathVariable(name = "id") Integer eventId) {
        log.info("Получен публичный запрос на поиск события с id= {}", eventId);
        return EventMapper.toEventFullDto(publicEventService.findById(request, eventId));
    }
}
