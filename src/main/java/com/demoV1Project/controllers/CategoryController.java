package com.demoV1Project.controllers;

import com.demoV1Project.dto.CategoryDto;
import com.demoV1Project.dto.UserDto;
import com.demoV1Project.model.Category;
import com.demoV1Project.model.User;
import com.demoV1Project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/category")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        List<CategoryDto> categoryDtoList = categoryService.findAll()
                .stream()
                .map(user -> CategoryDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .toList();
        return ResponseEntity.ok(categoryDtoList);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        Optional<Category> categoryOptional = categoryService.findById(id);

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            CategoryDto categoryDto = CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
            return ResponseEntity.ok(categoryDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CategoryDto categoryDto) throws URISyntaxException {
        Category category = (Category.builder()
                .name(categoryDto.getName())
                .build());
        categoryService.save(category);
        return ResponseEntity.created(new URI("/api/v0/category/save")).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryDto categoryDto){
        Optional<Category> categoryOptional = categoryService.findById(id);

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(categoryDto.getName());
            categoryService.save(category);

            return ResponseEntity.ok("Field Updated");
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        if (id != null) {
            categoryService.deleteById(id);
            return ResponseEntity.ok("Field Deleted");
        }
        return ResponseEntity.badRequest().build();
    }


}
