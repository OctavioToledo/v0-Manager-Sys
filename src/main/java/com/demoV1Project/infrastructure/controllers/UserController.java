package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.UserMapper;
import com.demoV1Project.application.service.UserService;
import com.demoV1Project.domain.dto.UserDto.UserCreateDto;
import com.demoV1Project.domain.dto.UserDto.UserDetailDto;
import com.demoV1Project.domain.dto.UserDto.UserDto;
import com.demoV1Project.domain.dto.UserDto.UserUpdateDto;
import com.demoV1Project.domain.model.Business;
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
@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/detail/{id}")
    public ResponseEntity<UserDetailDto> detailById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userMapper::toDetailDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<Long> save(@RequestBody UserCreateDto userCreateDto) throws URISyntaxException {
        User user = userMapper.toEntity(userCreateDto);
        User savedUser = userService.save(user);
        return ResponseEntity.created(new URI("/api/v0/user/save"))
                .body(savedUser.getId());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        User user = userService.findById(id).orElseThrow(()-> new IllegalArgumentException("User Not Found"));
        userMapper.updateEntity(userUpdateDto, user); // Usa el mapper para actualizar
        userService.save(user);
            return ResponseEntity.ok("User updated successfully");
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
