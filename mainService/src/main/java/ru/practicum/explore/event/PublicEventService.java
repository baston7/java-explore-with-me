package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.support.NullValue;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
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
        boolean isCategories= false;
        String textForSearch=null;
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
        if(text!=null){
            textForSearch=text.toLowerCase();
        }
        if(categories!=null){
            isCategories=true;
        }
        if (onlyAvailable) {
            events = eventRepository.findPublicEvents(textForSearch, categories, paid, start, end,isCategories, sorting, pageRequest);
        } else {
            events = eventRepository.findEventsWithParamsWithoutLimit(textForSearch, categories, paid, start, end,isCategories, sorting, pageRequest);
        }
        if (events.isEmpty()) {
            throw new RuntimeException("ne naideno sobytiy");
        }
        EndpointHitDto endpointHitDto = new EndpointHitDto("ExploreWithMe", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter));

        events.forEach(event -> event.setViews(event.getViews() + 1));
        eventRepository.saveAll(events);
        statsClient.post(endpointHitDto);
        return events;
    }

    public Event findById(HttpServletRequest request, Integer id) {
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED).orElseThrow(() -> new RuntimeException("ne naydeno"));
        EndpointHitDto endpointHitDto = new EndpointHitDto("ExploreWithMe", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter));
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        statsClient.post(endpointHitDto);
        return event;
    }
}
