package ru.practicum.explore.event;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto=new CompilationDto();
        compilationDto.setEvents(compilation.getEvents().stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()));
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event>events) {
        Compilation compilation=new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.isPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }
}
