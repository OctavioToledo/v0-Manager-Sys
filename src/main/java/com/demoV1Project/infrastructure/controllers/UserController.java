package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.UserMapper;
import com.demoV1Project.application.service.UserService;
import com.demoV1Project.domain.dto.UserDto.UserCreateDto;
import com.demoV1Project.domain.dto.UserDto.UserDetailDto;
import com.demoV1Project.domain.dto.UserDto.UserDto;
import com.demoV1Project.domain.dto.UserDto.UserUpdateDto;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.shared.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final TenantContext tenantContext;

    /**
     * Devuelve los datos del usuario autenticado actual.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDetailDto> getCurrentUser() {
        User user = tenantContext.getCurrentUser();
        return ResponseEntity.ok(userMapper.toDetailDto(user));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        User currentUser = tenantContext.getCurrentUser();
        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return userService.findById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<UserDetailDto> detailById(@PathVariable Long id) {
        User currentUser = tenantContext.getCurrentUser();
        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return userService.findById(id)
                .map(userMapper::toDetailDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        User currentUser = tenantContext.getCurrentUser();
        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userMapper.updateEntity(userUpdateDto, currentUser);
        userService.save(currentUser);
        return ResponseEntity.ok("User updated successfully");
    }
}
