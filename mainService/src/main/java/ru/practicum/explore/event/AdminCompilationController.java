package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final PrivateEventService privateEventService;

    @PostMapping
    public CompilationDto publishCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Получен администраторский запрос на публикацию подборки с названием: {} ",
                newCompilationDto.getTitle());
        List<Event> events = privateEventService.getAllByEventsIds(newCompilationDto.getEvents());
        Compilation compilation = compilationService.save(CompilationMapper.toCompilation(newCompilationDto, events));
        log.info("Подборка успешно опубликована");
        return CompilationMapper.toCompilationDto(compilation);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Integer compId) {
        log.info("Получен администраторский запрос на удаление подборки с id = {}", compId);
        compilationService.deleteById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Integer compId, @PathVariable Integer eventId) {
        log.info("Получен администраторский запрос на удаление события с id = {}, из подборки с id = {} ",
                eventId, compId);
        compilationService.deleteEventFromComp(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Integer compId, @PathVariable Integer eventId) {
        log.info("Получен администраторский запрос на добавление события с id = {}, в подборку с id = {} ",
                eventId, compId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Integer compId) {
        log.info("Получен администраторский запрос на открепление подборки на главной странице с id = {} ", compId);
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Integer compId) {
        log.info("Получен администраторский запрос на закрепление подборки на главной странице с id = {} ", compId);
        compilationService.pinCompilation(compId);
    }
}
