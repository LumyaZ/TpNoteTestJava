package com.example.tpnotetest.service;

import java.util.List;
import java.util.Optional;

import com.example.tpnotetest.entity.User;



public interface UserService {

	User getUserById(Long id);
	
    User createUser(User user);
    
    User updateUser(Long id, User user);
    
    void deleteUser(Long id);
    
    List<User> getAllUsers();

    boolean findByEmail(String email);

}
