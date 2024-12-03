package com.demoV1Project.service;

import com.demoV1Project.dto.UserDto;
import com.demoV1Project.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    Optional<User> findById(Long id);
    void save(User user);
    void deleteById(Long id);
    boolean existsById(Long id);


}
