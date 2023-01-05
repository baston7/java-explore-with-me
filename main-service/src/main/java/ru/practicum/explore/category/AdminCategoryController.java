package ru.practicum.explore.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.model.Category;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен администраторский запрос на добавление категории: {}", categoryDto.getName());
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryService.saveCategory(category));
    }

    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен администраторский запрос на изменение категории с id = {}", categoryDto.getId());
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryService.updateCategory(category));
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable(name = "catId") Integer categoryId) {
        log.info("Получен администраторский запрос на удаление категории с id = {}", categoryId);
        categoryService.deleteCategoryById(categoryId);
    }
}
