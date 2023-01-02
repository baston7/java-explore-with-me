package ru.practicum.explore.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.exception.CategoryNotFoundException;
import ru.practicum.explore.exception.ForbiddenException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("не найдено категорий"));
    }

    public List<Category> getCategories(int page, int size) {
        List<Category> categories = categoryRepository.findBy(PageRequest.of(page, size));
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Не найдено категорий");
        }
        log.info("Запрос обработан");
        return categories;
    }

    public Category saveCategory(Category category) {
        Category newCategory = categoryRepository.save(category);
        log.info("Категория создана c id={}", newCategory.getId());
        return newCategory;
    }

    public Category updateCategory(Category newCategory) {
        getCategoryById(newCategory.getId());
        Category category = categoryRepository.save(newCategory);
        log.info("Категория c id={} обновлена", newCategory.getId());
        return category;
    }

    public void deleteCategoryById(Integer catId) {
        getCategoryById(catId);
        if (eventRepository.findFirstByCategoryId(catId).isPresent()) {
            log.info("Категорию c id={} тк с ней связаны события ", catId);
            throw new ForbiddenException("Удаление запрощено. С категорией не должно быть связано ни одного события");
        }
        log.info("Категорию c id={} удалена", catId);
        categoryRepository.deleteById(catId);
    }

}
