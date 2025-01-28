package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.UserMapper;
import com.demoV1Project.application.service.UserService;
import com.demoV1Project.domain.dto.UserDto;
import com.demoV1Project.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(userMapper.toDtoList(users));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody UserDto userDto) throws URISyntaxException {
        User user = userMapper.toEntity(userDto);
        userService.save(user);
        return ResponseEntity.created(new URI("/api/v0/user/save")).body("User saved successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userDto.getName());
            user.setPassword(userDto.getPassword());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());

            userService.save(user);
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
