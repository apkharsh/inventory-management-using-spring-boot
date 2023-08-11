package com.apkharsh.inventory.controllers;

import com.apkharsh.inventory.models.User;
import com.apkharsh.inventory.service.UserService;
import com.apkharsh.inventory.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/all")
    public ResponseEntity<?> getUsers(@RequestBody String userID) {
//        only creator is authorised to get details of all users
        if(!userService.isCreator(userID)){
            return new ResponseEntity<>(new CustomResponse("You are not authorised to perform this action"), HttpStatus.UNAUTHORIZED);
        }

        List<User> users = userService.findAllUsers();

        if (users.size() > 0) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CustomResponse("No users are registered"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users/id/{id}")
    public ResponseEntity<?> findUserByID(@PathVariable("id") String userID, @RequestBody String adminID) {

        if(!userService.isCreator(adminID)){
            return new ResponseEntity<>(new CustomResponse("You are not authorised to perform this action"), HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findUserByID(userID);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CustomResponse("user does not exist with this ID"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users/email/{email}")
    public ResponseEntity<?> findUserByEmail(@PathVariable("email") String email, @RequestBody String adminID) {

        if(!userService.isCreator(adminID)){
            return new ResponseEntity<>(new CustomResponse("You are not authorised to perform this action"), HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findUserByEmail(email);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CustomResponse("user does not exist with this email"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users/new")
    public ResponseEntity<?> addUser(@RequestBody User user) {

        if (userService.addUser(user)) {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(new CustomResponse("Account already exists with this email"), HttpStatus.CONFLICT);
    }

    @DeleteMapping("/users/delete/{userID}")
    public ResponseEntity<?> deleteUser(@PathVariable("userID") String userID) {

        if (userService.deleteUser(userID)) {
            return new ResponseEntity<>(new CustomResponse("User " + userID + " deleted successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CustomResponse("user does not exist with this id " + userID), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {

        if (userService.loginUser(user)) {
            return new ResponseEntity<>(new CustomResponse("user is logged in "), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CustomResponse("invalid credentials"), HttpStatus.NOT_FOUND);
        }
    }

}
