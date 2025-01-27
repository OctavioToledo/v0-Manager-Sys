package com.demoV1Project.infrastructure.persistence;


import com.demoV1Project.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    Optional<Category> findById(Long id);
    List<Category> findAll();
    void save(Category category);
    //void update(Category category);
    void deleteById(Long id);
}
