package ru.practicum.explore.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.event.PrivateEventService;
import ru.practicum.explore.event.model.Event;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final PrivateEventService privateEventService;

    @PostMapping
    public CompilationDto publishCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Получен администраторский запрос на публикацию подборки с названием: {} ",
                newCompilationDto.getTitle());
        List<Event> events = privateEventService.getAllByEventsIds(newCompilationDto.getEvents());
        Compilation compilation = compilationService.save(CompilationMapper.toCompilation(newCompilationDto, events));
        log.info("Подборка успешно опубликована");
        return CompilationMapper.toCompilationDto(compilation);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable(name = "compId") Integer compilationId) {
        log.info("Получен администраторский запрос на удаление подборки с id = {}", compilationId);
        compilationService.deleteById(compilationId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable(name = "compId") Integer compilationId,
                                           @PathVariable(name = "eventId") Integer eventId) {
        log.info("Получен администраторский запрос на удаление события с id = {}, из подборки с id = {} ",
                eventId, compilationId);
        compilationService.deleteEventFromComp(compilationId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable(name = "compId") Integer compilationId,
                                      @PathVariable(name = "eventId") Integer eventId) {
        log.info("Получен администраторский запрос на добавление события с id = {}, в подборку с id = {} ",
                eventId, compilationId);
        compilationService.addEventToCompilation(compilationId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable(name = "compId") Integer compilationId) {
        log.info("Получен администраторский запрос на открепление подборки на главной странице с id = {} ",
                compilationId);
        compilationService.unpinCompilation(compilationId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable(name = "compId") Integer compilationId) {
        log.info("Получен администраторский запрос на закрепление подборки на главной странице с id = {} ",
                compilationId);
        compilationService.pinCompilation(compilationId);
    }
}
