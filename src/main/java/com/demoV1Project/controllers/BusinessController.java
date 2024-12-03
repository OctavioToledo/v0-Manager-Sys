package com.demoV1Project.controllers;

import com.demoV1Project.dto.BusinessDto;
import com.demoV1Project.model.Address;
import com.demoV1Project.model.Business;
import com.demoV1Project.model.Category;
import com.demoV1Project.model.User;
import com.demoV1Project.service.AddressService;
import com.demoV1Project.service.BusinessService;
import com.demoV1Project.service.CategoryService;
import com.demoV1Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/business")
public class BusinessController {

    @Autowired
    private final BusinessService businessService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final AddressService addressService;

    public BusinessController(BusinessService businessService, UserService userService, CategoryService categoryService, AddressService addressService) {
        this.businessService = businessService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.addressService = addressService;
    }


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
                    .build();
            return ResponseEntity.ok(businessDto);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody BusinessDto businessDto) throws URISyntaxException {

        // Validación del User
        if (businessDto.getUserId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        Optional<User> userOptional = userService.findById(businessDto.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOptional.get();

        // Validación del Category
        Category category = null;
        if (businessDto.getCategory() != null && businessDto.getCategory().getId() != null) {
            Optional<Category> categoryOptional = categoryService.findById(businessDto.getCategory().getId());
            if (categoryOptional.isPresent()) {
                category = categoryOptional.get();
            } else {
                return ResponseEntity.badRequest().body("Category not found");
            }
        }

        // Validación del Address
        Address address = null;
        if (businessDto.getAddress() != null && businessDto.getAddress().getId() != null) {
            Optional<Address> addressOptional = addressService.findById(businessDto.getAddress().getId());
            if (addressOptional.isPresent()) {
                address = addressOptional.get();
            } else {
                return ResponseEntity.badRequest().body("Address not found");
            }
        }

        // Creación de Business
        Business business = Business.builder()
                .name(businessDto.getName())
                .description(businessDto.getDescription())
                .phoneNumber(businessDto.getPhoneNumber())
                .logo(businessDto.getLogo())
                .openingHours(businessDto.getOpeningHours())
                .workDays(businessDto.getWorkDays())
                .user(user)
                .category(category) // Asociar Category existente
                .address(address)   // Asociar Address existente
                .build();

        // Guardar el negocio
        businessService.save(business);

        return ResponseEntity.created(new URI("/api/v0/business/save")).build();
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BusinessDto businessDto){
        Optional<Business> businessOptional = businessService.findById(id);

        if (businessOptional.isPresent()) {
            Business business = businessOptional.get();
                    business.setName(businessDto.getName());
                    business.setDescription(businessDto.getDescription());
                    business.setPhoneNumber(businessDto.getPhoneNumber());
                    business.setLogo(businessDto.getLogo());
                    business.setOpeningHours(businessDto.getOpeningHours());
                    business.setWorkDays(businessDto.getWorkDays());
                    business.setUser(businessDto.getUser());

                    businessService.save(business);

            return ResponseEntity.ok("Field Updated");
        }
        return ResponseEntity.badRequest().build();
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
