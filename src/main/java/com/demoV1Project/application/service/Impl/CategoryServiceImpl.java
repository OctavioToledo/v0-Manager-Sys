package com.demoV1Project.application.service.Impl;


import com.demoV1Project.domain.model.Category;
import com.demoV1Project.infrastructure.persistence.CategoryDao;
import com.demoV1Project.application.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private final CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    @Override
    public Optional<Category> findById(Long id) {
        return categoryDao.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryDao.findByName(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public void save(Category category) {
        categoryDao.save(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryDao.deleteById(id);
    }
}
