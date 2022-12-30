package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> findAll(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен публичный запрос на просмотр всех категорий");
        return categoryService.getCategories(from / size, size);
    }

    @GetMapping("/{catId}")
    public Category findById(@PathVariable Integer catId) {
        log.info("Получен публичный запрос на просмотр категории с id= {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
