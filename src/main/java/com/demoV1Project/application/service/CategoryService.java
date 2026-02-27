package com.demoV1Project.application.service;

import com.demoV1Project.domain.model.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Optional<Category> findById(Long id);

    List<Category> findAll();

    Page<Category> findAll(Pageable pageable);

    Category save(Category category);

    // void update(Category category);
    void deleteById(Long id);

    Optional<Category> findByName(String name);

    Category findByIdOrThrow(Long id);
}
