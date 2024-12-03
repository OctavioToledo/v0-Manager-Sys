package com.demoV1Project.service;


import com.demoV1Project.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findById(Long id);
    List<Category> findAll();
    void save(Category category);
    //void update(Category category);
    void deleteById(Long id);
}
