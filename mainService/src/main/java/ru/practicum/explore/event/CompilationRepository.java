package ru.practicum.explore.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    List<Compilation> findAllByPinned(boolean pinned, PageRequest pageRequest);
    List<Compilation> findAllBy(PageRequest pageRequest);
}
