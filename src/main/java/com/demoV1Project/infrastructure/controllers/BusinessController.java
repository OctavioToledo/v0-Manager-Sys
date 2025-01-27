package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.dto.BusinessDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.application.service.AddressService;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.CategoryService;
import com.demoV1Project.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/business")
@RequiredArgsConstructor
public class BusinessController {

    @Autowired
    private final BusinessService businessService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final AddressService addressService;



    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
    List<BusinessDto> businessList = businessService.findAll()
            .stream()
            .map(business -> BusinessDto.builder()
                    .id(business.getId())
                    .name(business.getName())
                    .description(business.getDescription())
                    .phoneNumber(business.getPhoneNumber())
                    .logo(business.getLogo())
                    .openingHours(business.getOpeningHours())
                    .workDays(business.getWorkDays())
                    .user(business.getUser())
                    .category(business.getCategory())
                    .address(business.getAddress())
                    .build())
            .toList();
    return ResponseEntity.ok(businessList);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        Optional<Business> businessOptional = businessService.findById(id);

        if (businessOptional.isPresent()) {
            Business business = businessOptional.get();
            BusinessDto businessDto = BusinessDto.builder()
                    .id(business.getId())
                    .name(business.getName())
                    .description(business.getDescription())
                    .phoneNumber(business.getPhoneNumber())
                    .logo(business.getLogo())
                    .openingHours(business.getOpeningHours())
                    .workDays(business.getWorkDays())
                    .user(business.getUser())
                    .category(business.getCategory())
                    .address(business.getAddress())
                    .build();
            return ResponseEntity.ok(businessDto);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody BusinessDto businessDto) throws URISyntaxException {

        // Validación del usuario
        if (businessDto.getUser() == null) {
            return ResponseEntity.badRequest().body("User cannot be null");
        }
/*

        User user = userService.findById(businessDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validación del Category (opcional)
        Category category = null;
        if (businessDto.getCategory() != null && businessDto.getCategory().getId() != null) {
            category = categoryService.findById(businessDto.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        }

        // Validación del Address (opcional)
        Address address = null;
        if (businessDto.getAddress() != null && businessDto.getAddress().getId() != null) {
            address = addressService.findById(businessDto.getAddress().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        }
 */
        // Construcción del objeto Business
        Business business = Business.builder()
                .name(businessDto.getName())
                .description(businessDto.getDescription())
                .phoneNumber(businessDto.getPhoneNumber())
                .logo(businessDto.getLogo())
                .openingHours(businessDto.getOpeningHours())
                .workDays(businessDto.getWorkDays())
                .user(businessDto.getUser())  // Asociar el usuario
                .category(businessDto.getCategory()) // Asociar categoría
                .address(businessDto.getAddress())   // Asociar dirección
                .build();

        // Guardar el negocio
        businessService.save(business);

        return ResponseEntity.created(new URI("/api/v0/business/save/")).body("Business created successfully");
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BusinessDto businessDto) {

        Business business = businessService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));
/*
        // Validación del usuario
        if (businessDto.getUserId() != null) {
            User user = userService.findById(businessDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            business.setUser(user);
        }

        // Validación del Category (opcional)
        if (businessDto.getCategory() != null && businessDto.getCategory().getId() != null) {
            Category category = categoryService.findById(businessDto.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            business.setCategory(category);
        }

        // Validación del Address (opcional)
        if (businessDto.getAddress() != null && businessDto.getAddress().getId() != null) {
            Address address = addressService.findById(businessDto.getAddress().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found"));
            business.setAddress(address);
        }
 */
        // Actualización de los campos restantes
        business.setName(businessDto.getName());
        business.setDescription(businessDto.getDescription());
        business.setPhoneNumber(businessDto.getPhoneNumber());
        business.setLogo(businessDto.getLogo());
        business.setOpeningHours(businessDto.getOpeningHours());
        business.setWorkDays(businessDto.getWorkDays());
        business.setUser(businessDto.getUser());
        business.setCategory(businessDto.getCategory());
        business.setAddress(businessDto.getAddress());
        // Guardar el negocio actualizado
        businessService.save(business);

        return ResponseEntity.ok("Business updated successfully");
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        if (id != null) {
            businessService.deleteById(id);
            return ResponseEntity.ok("Field Deleted");
        }
        return ResponseEntity.badRequest().build();
    }

}
