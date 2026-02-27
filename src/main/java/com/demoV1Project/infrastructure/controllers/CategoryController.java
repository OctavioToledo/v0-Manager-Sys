package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.dto.CategoryDto.CategoryDto;
import com.demoV1Project.domain.dto.CategoryDto.CategoryUpdateDto;
import com.demoV1Project.domain.model.Category;
import com.demoV1Project.application.service.CategoryService;
import com.demoV1Project.application.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/findAll")
    public ResponseEntity<org.springframework.data.domain.Page<CategoryDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size, org.springframework.data.domain.Sort.by("name").ascending());
        org.springframework.data.domain.Page<CategoryDto> result = categoryService.findAll(pageable)
                .map(categoryMapper::toDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(categoryMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Long> save(@RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.created(URI.create("/api/v1/category/save"))
                .body(savedCategory.getId());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CategoryUpdateDto categoryUpdateDto) {
        return categoryService.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryUpdateDto.getName());
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