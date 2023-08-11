package com.apkharsh.inventory.service;

import com.apkharsh.inventory.models.User;
import com.apkharsh.inventory.repositories.UserRepo;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.repository.Near;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @Test
    public void findAllUsers_test() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(new User("harsh@gmail.com", "123"));
        userList.add(new User("naveen@gmail.com", "456"));

        when(userRepo.findAll()).thenReturn(userList);

        List<User> result = userService.findAllUsers();

        // Assert
        assertNotNull(result); // Ensure that the result is not null
        assertEquals(2, result.size()); // Ensure that the result contains two users
        assertEquals("harsh@gmail.com", result.get(0).getEmail()); // Ensure that the first user's name is "John"
        assertEquals("naveen@gmail.com", result.get(1).getEmail()); // Ensure that the second user's name is "Alice"

        // Verify that the userRepo.findAll() method was called exactly once
        verify(userRepo, times(1)).findAll();
    }

    @Nested
    public class FindUserByIDTests{
        @Test
        public void findUserByID_UserExists_test() {
            // Arrange
            String userID = "123";
            User user = new User(userID,"john@gmail.com", "John");

            when(userRepo.findById(userID)).thenReturn(Optional.of(user));

            // Act
            User result = userService.findUserByID(userID);

            // Assert
            assertNotNull(result); // Ensure that the result is not null
            assertEquals(userID, result.getID()); // Ensure that the result has the correct userID
            assertEquals("John", result.getPassword()); // Ensure that the result has the correct user name

            // Verify that the userRepo.findById() method was called exactly once with the correct userID
            verify(userRepo, times(1)).findById(userID);
        }

        @Test
        public void findUserByID_UserNotExists_test() {

            String userID = "456";

            when(userRepo.findById(userID)).thenReturn(Optional.empty());

            User result = userService.findUserByID(userID);

            assertNull(result); // Ensure that the result is null since the user does not exist

            // Verify that the userRepo.findById() method was called exactly once with the correct userID
            verify(userRepo, times(1)).findById(userID);
        }

    }

    @Nested
    public class FindUserByEmailTests{
        @Test
        public void findUserByEmail_UserExists_test() {

            String email = "john@example.com";
            User user = new User("123", email, "1234");

            when(userRepo.findByEmail(email)).thenReturn(user);

            User result = userService.findUserByEmail(email);

            assertNotNull(result); // Ensure that the result is not null
            assertEquals(email, result.getEmail()); // Ensure that the result has the correct email

            // Verify that the userRepo.findByEmail() method was called exactly once with the correct email
            verify(userRepo, times(1)).findByEmail(email);
        }

        @Test
        public void findUserByEmail_UserNotExists_test() {

            String email = "notfound@example.com";

            when(userRepo.findByEmail(email)).thenReturn(null);
            User result = userService.findUserByEmail(email);

            assertNull(result); // Ensure that the result is null since the user does not exist

            // Verify that the userRepo.findByEmail() method was called exactly once with the correct email
            verify(userRepo, times(1)).findByEmail(email);
        }

    }

    @Nested
    public class LoginUserTests{
        @Test
        public void login_correct_credentials_test(){

            String email = "abc@gmail.com";
            String password = "1234";
            User user = new User("123", email, password);

            when(userRepo.findByEmail(email)).thenReturn(user);

            boolean result = userService.loginUser(user);
            assertTrue(result);
            assertEquals(user.getPassword(), password);

        }

        @Test
        public void login_wrong_password_test(){

            String email = "abc@gmail.com";
            String correctPassword = "1234";
            String wrongPassword = "2222";

            User user = new User("123", email, correctPassword);

            when(userRepo.findByEmail(email)).thenReturn(user);

            boolean result = userService.loginUser(new User("123", email, wrongPassword));
            assertFalse(result);
            verify(userRepo, times(1)).findByEmail(email);

        }

    }

    @Nested
    public class DeleteUserTests{
        @Test
        public void deleteUser_UserExists_Test(){
            String userID = "123";
            User user = new User(userID,"john@gmail.com", "John");
            when(userRepo.findById(anyString())).thenReturn(Optional.of(user));

            boolean actual = userService.deleteUser(user.getID());

            assertTrue(actual);
            verify(userRepo, times(1)).deleteById(anyString());

        }

        @Test
        public void deleteUser_UserNotExist_Test(){
            String userID = "123";
            User user = new User(userID,"john@gmail.com", "John");
            when(userRepo.findById(anyString())).thenReturn(Optional.empty());

            boolean actual = userService.deleteUser(user.getID());

            assertFalse(actual);
            verify(userRepo, times(1)).findById(anyString());
        }

    }
    @Nested
    public class AddUserTests{
        @Test
        public void addUser_user_does_not_exist_test(){
            String userID = "123";
            User user = new User(userID,"john@gmail.com", "John");
            when(userRepo.findByEmail(anyString())).thenReturn(null);

            boolean actual = userService.addUser(user);

            verify(userRepo, times(1)).save(any());
            assertTrue(actual);
        }

        @Test
        public void addUser_user_already_exist_test(){
            String userID = "123";
            User user = new User(userID,"john@gmail.com", "John");
            when(userRepo.findByEmail(anyString())).thenReturn(user);

            boolean actual = userService.addUser(user);

            assertFalse(actual);
        }
    }

}
