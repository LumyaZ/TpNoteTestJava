package com.example.tpnotetest.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tpnotetest.entity.User;
import com.example.tpnotetest.exception.DataIntegrityViolationException;
import com.example.tpnotetest.exception.ObjectNotFoundException;
import com.example.tpnotetest.repository.UserRepository;
import com.example.tpnotetest.service.UserService;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    @Override
    public boolean findByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }
	
	 @Override
	    public User getUserById(Long id) {
	        return userRepo.findById(id)
	                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
	    }

	    @Override
	    public User createUser(User user) {
	        if (this.findByEmail(user.getEmail())) {
	            throw new DataIntegrityViolationException("Email already exists");
	        }
	        return userRepo.save(user);
	    }

	    @Override
	    public User updateUser(Long id, User user) {
	        User existingUser = userRepo.findById(id)
	                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
	        existingUser.setName(user.getName());
	        existingUser.setEmail(user.getEmail());
	        existingUser.setPassword(user.getPassword());
	        return userRepo.save(existingUser);
	    }

	    @Override
	    public void deleteUser(Long id) {
	        User user = userRepo.findById(id)
	                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
	        userRepo.delete(user);
	    }

	    @Override
	    public List<User> getAllUsers() {
	        return userRepo.findAll();
	    }
}
