package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicEventService {
    private final EventRepository eventRepository;

    public List<Event> findAll(String text,
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
        LocalDateTime end ;
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

        if (onlyAvailable) {
            events = eventRepository.findPublicEvents(text.toLowerCase(), categories, paid, start, end, sorting, pageRequest);
        } else {
            events = eventRepository.findEventsWithParamsWithoutLimit(text.toLowerCase(), categories, paid, start, end, sorting, pageRequest);
        }
        if(events.isEmpty()){
            throw new RuntimeException("ne naideno sobytiy");
        }
        return events;
    }
    public Event findById(Integer id){
        return eventRepository.findByIdAndState(id,State.PUBLISHED).orElseThrow(()->new RuntimeException("ne naydeno"));
    }
}
