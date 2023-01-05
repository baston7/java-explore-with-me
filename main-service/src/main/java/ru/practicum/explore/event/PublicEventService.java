package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.client.StatsClient;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.exception.EventNotFoundException;
import ru.practicum.explore.hit.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicEventService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Event> findAll(HttpServletRequest request, String text,
                               List<Integer> categories,
                               Boolean paid,
                               String rangeStart,
                               String rangeEnd,
                               String sort,
                               boolean onlyAvailable,
                               PageRequest pageRequest) {
        List<Event> events;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start;
        LocalDateTime end;
        boolean isCategories = false;
        String textForSearch = null;
        Sort sorting;
        if (sort == null || sort.equals("EVENT_DATE")) {
            sorting = Sort.by(Sort.Direction.DESC, "eventDate");
        } else {
            sorting = Sort.by(Sort.Direction.DESC, "views");
        }
        if (rangeStart == null && rangeEnd == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.now().plusYears(300);
        } else if (rangeStart == null && rangeEnd != null) {
            start = LocalDateTime.now().minusYears(300);
            end = LocalDateTime.parse(rangeEnd, formatter);
        } else if (rangeEnd == null) {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.now().plusYears(300);
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        if (text != null) {
            textForSearch = text.toLowerCase();
        }
        if (categories != null) {
            isCategories = true;
        }
        if (onlyAvailable) {
            events = eventRepository.findPublicEvents(textForSearch, categories, paid, start, end, isCategories,
                    sorting, pageRequest);
        } else {
            events = eventRepository.findEventsWithParamsWithoutLimit(textForSearch, categories, paid, start, end,
                    isCategories, sorting, pageRequest);
        }
        if (events.isEmpty()) {
            throw new EventNotFoundException("Не найдено событий под данные условия");
        }
        EndpointHitDto endpointHitDto = new EndpointHitDto("ExploreWithMe", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter));
        events.forEach(event -> event.setViews(event.getViews() + 1));
        eventRepository.saveAll(events);
        statsClient.post(endpointHitDto);
        log.info("Запрос на получение списка событий обработан");
        log.info("Отправлен запрос на сервер статистики");
        return events;
    }

    public Event findById(HttpServletRequest request, Integer id) {
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException("Не найдено событие"));
        EndpointHitDto endpointHitDto = new EndpointHitDto("ExploreWithMe", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter));
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        statsClient.post(endpointHitDto);
        log.info("Запрос на получение события обработан");
        log.info("Отправлен запрос на сервер статистики");
        return event;
    }
}
