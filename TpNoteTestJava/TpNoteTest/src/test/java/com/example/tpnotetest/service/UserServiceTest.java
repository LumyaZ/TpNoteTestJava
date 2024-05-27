package com.example.tpnotetest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.tpnotetest.entity.User;
import com.example.tpnotetest.exception.DataIntegrityViolationException;
import com.example.tpnotetest.exception.ObjectNotFoundException;
import com.example.tpnotetest.repository.UserRepository;
import com.example.tpnotetest.serviceImpl.UserServiceImpl;

public class UserServiceTest {
	@Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetUserByIdWithUserNotFound() {
        Long userId = 1L;
        String expectedMessage = "User not found";

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    public void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "Thomas", "thomas@gmail.com", "cornu"));

        when(userRepo.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Thomas");
        user.setEmail("thomas@gmail.com");

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        User returnedUser = userService.getUserById(user.getId());

        // Assert
        assertNotNull(returnedUser);
        assertEquals(User.class, returnedUser.getClass());
        assertEquals(userId, returnedUser.getId());
        assertEquals("Thomas", returnedUser.getName());
        assertEquals("thomas@gmail.com", returnedUser.getEmail());
    }
    
    @Test
    public void testSaveUser() {
        // Arrange
        User userToSave = new User();
        userToSave.setId(1L);
        userToSave.setName("Thomas");
        userToSave.setEmail("thomas@gmail.com");
        userToSave.setPassword("cornu");

        when(userRepo.save(any(User.class))).thenReturn(userToSave);

        User savedUser = userService.createUser(userToSave);
        assertNotNull(savedUser);
        assertEquals(User.class, savedUser.getClass());
        assertEquals(userToSave.getName(), savedUser.getName());
        assertEquals(userToSave.getEmail(), savedUser.getEmail());
        assertEquals(userToSave.getPassword(), savedUser.getPassword());
    }

    @Test
    public void testSaveUserEmailExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Thomas");
        existingUser.setEmail("thomas@gmail.com");
        existingUser.setPassword("cornu");

        when(userRepo.save(existingUser)).thenThrow(new DataIntegrityViolationException("Email exists"));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userService.createUser(existingUser);
        });

        assertEquals("Email exists", exception.getMessage());
    }
    
    @Test
    public void testUpdateUser() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Thomas");
        existingUser.setEmail("thomas@gmail.com");
        existingUser.setPassword("cornu");
        
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setName("Axelis");
        updateUser.setEmail("axelis@gmail.com");
        updateUser.setPassword("neri");

        when(userRepo.findById(1L)).thenReturn(Optional.of(existingUser));
        User userById = userService.getUserById(existingUser.getId());
        
		when(userRepo.save(any(User.class))).thenReturn(userById);
		User user = userService.updateUser(1L, updateUser);
		
        assertNotNull(user);
        assertEquals(User.class, user.getClass());
        assertEquals("Axelis", user.getName());
        assertEquals("axelis@gmail.com", user.getEmail());
        assertEquals("neri", user.getPassword());
    }
    
    /*@Test
    void testUpdateUserEmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Thomas");
        existingUser.setEmail("thomas@gmail.com");

        User newUser = new User();
        newUser.setId(2L);
        newUser.setName("Axelis");
        newUser.setEmail("axelis@gmail.com");

        when(userRepo.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepo.findByEmail("axelis@gmail.com")).thenReturn(Optional.of(newUser));

        User user = new User();
        user.setName("Axelis");
        user.setEmail("axelis@gmail.com");

        assertThrows(DataIntegrityViolationException.class, () -> userService.updateUser(1L, user));
    }*/

    @Test
	public void testDeleteUser() {
    	User user = new User();
    	user.setId(1L);
    	user.setName("Thomas");
    	user.setEmail("thomas@gmail.com");
    	user.setPassword("cornu");
         
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		
		doNothing().when(userRepo).deleteById(1L);
		
		userService.deleteUser(1L);
		
		verify(userRepo, times(2)).findById(1L);
		verify(userRepo, times(1)).deleteById(1L);
	}
    
    @Test
    void testDeleteUserUserNotFound() {
        when(userRepo.existsById(anyLong())).thenReturn(false);
        assertThrows(ObjectNotFoundException.class, () -> userService.deleteUser(1L));
    }
    
}
