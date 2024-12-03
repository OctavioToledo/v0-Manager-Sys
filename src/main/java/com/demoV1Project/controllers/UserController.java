package com.demoV1Project.controllers;



import com.demoV1Project.dto.UserDto;
import com.demoV1Project.model.User;
import com.demoV1Project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/user")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        List<UserDto> usersList = userService.findAll()
                .stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .password(user.getPassword())
                        .phoneNumber(user.getPhoneNumber())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .businessList(user.getBusinessList())
                        .build())
                .toList();
        return ResponseEntity.ok(usersList);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .password(user.getPassword())
                    .phoneNumber(user.getPhoneNumber())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .businessList(user.getBusinessList())
                    .build();
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserDto userDto) throws URISyntaxException {

        User user = (User.builder()
                .name(userDto.getName())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build());
        userService.save(user);

        return ResponseEntity.created(new URI("/api/v0/user/save")).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDto userDto){
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userDto.getName());
            user.setPassword(userDto.getPassword());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());
            user.setBusinessList(user.getBusinessList());

            userService.save(user);

            return ResponseEntity.ok("Field Updated");
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        if (id != null) {
            userService.deleteById(id);
            return ResponseEntity.ok("Field Deleted");
        }
        return ResponseEntity.badRequest().build();
    }

}
