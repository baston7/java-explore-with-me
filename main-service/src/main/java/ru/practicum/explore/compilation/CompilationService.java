package ru.practicum.explore.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.exception.CompilationNotFoundException;
import ru.practicum.explore.exception.EventNotFoundException;
import ru.practicum.explore.exception.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public Compilation save(Compilation compilation) {
        Compilation newCompilation = compilationRepository.save(compilation);
        log.info("Подборка создана c id={}", newCompilation.getId());
        return newCompilation;
    }

    public Compilation getById(Integer compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Не найдено подборки"));
    }

    public void deleteById(Integer compId) {
        getById(compId);
        compilationRepository.deleteById(compId);
    }

    public void deleteEventFromComp(Integer compId, Integer eventId) {
        Compilation compilation = getById(compId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Не найдено события"));

        List<Event> events = compilation.getEvents();
        if (events.isEmpty()) {
            throw new EventNotFoundException("Событий в подборке нет");
        }
        if (!events.remove(event)) {
            throw new EventNotFoundException("Не найдено такого события для удаления");
        }
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        log.info("Событие c id={} удалено из подборки ", eventId);
    }

    public void addEventToCompilation(Integer compId, Integer eventId) {
        Compilation compilation = getById(compId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Не найдено такого события"));

        List<Event> events = compilation.getEvents();
        if (events.contains(event)) {
            throw new ValidationException("Событие уже есть в подборке");
        }
        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        log.info("Событие c id={} добавлено в подборку ", eventId);
    }

    public void unpinCompilation(Integer compId) {
        Compilation compilation = getById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Подборка c id={} откреплена", compId);
    }

    public void pinCompilation(Integer compId) {
        Compilation compilation = getById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Подборка c id={} закреплена", compId);
    }

    public List<Compilation> findAllByPinned(boolean pinned, int page, int size) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(page, size));
        if (compilations.isEmpty()) {
            throw new CompilationNotFoundException("Не найдено подборок");
        }
        log.info("Запрос обработан");
        return compilations;
    }

    public List<Compilation> findAll(int page, int size) {
        List<Compilation> compilations = compilationRepository.findAllBy(PageRequest.of(page, size));
        if (compilations.isEmpty()) {
            throw new CompilationNotFoundException("Не найдено подборок");
        }
        log.info("Запрос обработан");
        return compilations;
    }

    public Compilation findById(int compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Не найдено такой подборки"));
    }
}
