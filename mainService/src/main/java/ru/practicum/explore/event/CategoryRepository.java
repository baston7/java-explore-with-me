package ru.practicum.explore.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select id from Category")
    List<Integer> findCategoriesIds();
    List<Category>findBy(PageRequest pageRequest);
}