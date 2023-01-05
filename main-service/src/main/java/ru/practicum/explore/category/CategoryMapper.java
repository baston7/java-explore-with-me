package ru.practicum.explore.category;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(),
                categoryDto.getName());
    }
}
