package com.demoV1Project.application.service;

import com.demoV1Project.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
    boolean existsById(Long id);
    User findByIdOrThrow(Long id);


}
