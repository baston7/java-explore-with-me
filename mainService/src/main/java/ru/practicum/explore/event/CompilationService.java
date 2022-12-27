package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public Compilation save(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    public Compilation getById(Integer compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new RuntimeException("Не найдено подборки"));
    }

    public void deleteById(Integer compId) {
        getById(compId);
        compilationRepository.deleteById(compId);
    }

    public void deleteEventFromComp(Integer compId, Integer eventId) {
        Compilation compilation = getById(compId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Не найдено такого события"));

        List<Event> events = compilation.getEvents();
        if (events.isEmpty()) {
            throw new RuntimeException("И так нет событий");
        }
        if (!events.remove(event)) {
            throw new RuntimeException("Не найдено такого события");
        }
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    public void addEventToCompilation(Integer compId, Integer eventId) {
        Compilation compilation = getById(compId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Не найдено такого события"));

        List<Event> events = compilation.getEvents();
        if (events.contains(event)) {
            throw new RuntimeException("Событие уже есть в подборке");
        }
        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    public void unpinCompilation(Integer compId) {
        Compilation compilation = getById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }
    public void pinCompilation(Integer compId) {
        Compilation compilation = getById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    public List<Compilation> findAllByPinned(boolean pinned, int page, int size) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(page, size));
        if (compilations.isEmpty()) {
            throw new RuntimeException("Не найдено событий");
        }
        return compilations;
    }
    public List<Compilation> findAll( int page, int size) {
        List<Compilation> compilations = compilationRepository.findAllBy(PageRequest.of(page, size));
        if (compilations.isEmpty()) {
            throw new RuntimeException("Не найдено событий");
        }
        return compilations;
    }

    public Compilation findById(int compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new RuntimeException("Не найдено такой подборки"));
    }
}
