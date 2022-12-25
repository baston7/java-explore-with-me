package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    public Category getCategoryById (int id) {
        return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("нет категории"));
    }
    public Category saveCategory (Category category) {
        return categoryRepository.save(category);
    }
    public Category updateCategory (Category newCategory) {
        getCategoryById(newCategory.getId());
        return categoryRepository.save(newCategory);
    }
    public void deleteCategoryById (Integer catId) {
        getCategoryById(catId);
        if(eventRepository.findFirstByCategoryId(catId).isPresent()){
            throw new RuntimeException("с категорией не должно быть связано ни одного события");
        }
        categoryRepository.deleteById(catId);
    }

}
