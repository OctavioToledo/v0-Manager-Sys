package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.CategoryDto;
import com.demoV1Project.domain.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CategoryDto toDto(Category category){
        return modelMapper.map(category, CategoryDto.class);
    }
    public Category toEntity(CategoryDto categoryDto){
        return modelMapper.map(categoryDto, Category.class);
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
