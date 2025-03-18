package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.dto.CategoryDto.CategoryDto;
import com.demoV1Project.domain.model.Category;
import com.demoV1Project.application.service.CategoryService;
import com.demoV1Project.application.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v0/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/findAll")
    public ResponseEntity<List<CategoryDto>> findAll() {
        List<Category> categories = categoryService.findAll();
        List<CategoryDto> categoryDtos = categoryMapper.toDtoList(categories);
        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(categoryMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        categoryService.save(category);
        return ResponseEntity.created(URI.create("/api/v0/category/save")).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryDto.getName());
                    categoryService.save(existingCategory);
                    return ResponseEntity.ok("Field Updated");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (id != null && categoryService.findById(id).isPresent()) {
            categoryService.deleteById(id);
            return ResponseEntity.ok("Field Deleted");
        }
        return ResponseEntity.notFound().build();
    }
}