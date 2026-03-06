package com.demoV1Project.config;

import com.demoV1Project.domain.model.Category;
import com.demoV1Project.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            List<String> defaultCategories = Arrays.asList(
                    "Barbería",
                    "Peluquería",
                    "Estética",
                    "Centro médico",
                    "Psicología",
                    "Odontología",
                    "Spa",
                    "Gimnasio",
                    "Clínica veterinaria",
                    "Centro de masajes");

            defaultCategories.forEach(name -> {
                categoryRepository.save(Category.builder().name(name).build());
            });

            System.out.println("Categorías por defecto creadas.");
        }
    }
}
