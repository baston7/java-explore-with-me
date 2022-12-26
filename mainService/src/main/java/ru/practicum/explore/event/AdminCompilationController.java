package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AdminCompilationService adminCompilationService;
    private final PrivateEventService privateEventService;

    @PostMapping
    public CompilationDto publishCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        List<Event> events=privateEventService.getAllByEventsIds(newCompilationDto.getEvents());
        Compilation compilation = adminCompilationService.saveCompilation(CompilationMapper.toCompilation(newCompilationDto,events));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
