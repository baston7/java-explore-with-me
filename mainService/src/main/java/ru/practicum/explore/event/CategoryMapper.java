package ru.practicum.explore.event;

import lombok.experimental.UtilityClass;

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
