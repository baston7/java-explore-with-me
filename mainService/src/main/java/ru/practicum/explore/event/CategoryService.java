package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    public Category getCategoryById (int id) {
        return categoryRepository.findById(id).orElseThrow(()-> new UserNotFoundException("нет категории"));
    }
    public List<Category> getCategories (int page, int size) {
        List<Category> categories= categoryRepository.findBy(PageRequest.of(page,size));
        if(categories.isEmpty()){
            throw new RuntimeException("Не найдено категорий");
        }
        return categories;
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
