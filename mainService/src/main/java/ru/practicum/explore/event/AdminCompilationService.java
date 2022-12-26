package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminCompilationService {
    private final CompilationRepository compilationRepository;

    public Compilation saveCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }
}
