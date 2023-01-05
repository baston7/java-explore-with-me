package ru.practicum.explore.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.category.CategoryMapper;
import ru.practicum.explore.category.CategoryService;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.event.EventMapper;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.model.Event;

import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor

public class AdminCommentController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}/publish")
    public EventFullDto publishComment(@PathVariable(name = "commentId") Integer commentId) {
        log.info("Получен администраторский запрос на публикацию события с id= {} ", commentId);
        log.info("Событие успешно опубликовано");
        return null;
    }
    @PatchMapping("/{commentId}/reject")
    public EventFullDto cancelEvent(@PathVariable(name = "commentId") Integer commentId,) {
        log.info("Получен администраторский запрос на отклонение события с id= {} ", commentId);
        log.info("Событие успешно отклонено");
        return null;
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
