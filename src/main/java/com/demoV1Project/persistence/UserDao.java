package com.demoV1Project.persistence;


import com.demoV1Project.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAll();
    Optional<User> findById(Long id);
    void save(User user);
    void deleteById(Long id);
    boolean existsById(Long id);


}
