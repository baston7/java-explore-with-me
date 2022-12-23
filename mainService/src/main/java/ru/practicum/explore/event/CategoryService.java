package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public Category getCategoryById (int id) {
        return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("нет категории"));
    }
    public Category saveCategory (Category category) {
        return categoryRepository.save(category);
    }
}
