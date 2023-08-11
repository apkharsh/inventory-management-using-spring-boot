package com.apkharsh.inventory.service;

import com.apkharsh.inventory.models.User;
import com.apkharsh.inventory.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public boolean isCreator(String ID){
        User user = findUserByID(ID);
        return user != null && user.getRole().equals("creator");
    }

    public boolean isAuthorized(String userID){
        User userExists = findUserByID(userID);
        if(userExists == null) return false;

        return userExists.getRole().equals("admin");
    }

    public boolean addUser(User user){

        boolean userExists = findUserByEmail(user.getEmail()) != null;

        if(userExists) return false;

        user.setID(UUID.randomUUID().toString());

        userRepo.save(user);
        return true;
    }

    public boolean deleteUser(String userID){

        boolean userExists = findUserByID(userID) != null;

        if(userExists){
            userRepo.deleteById(userID);
            return true;
        }

        return false;
    }

    public List<User> findAllUsers(){
        return userRepo.findAll();
    }

    public User findUserByID(String userID){
        return userRepo.findById(userID).orElse(null);
    }

    public User findUserByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public boolean loginUser(User user){

        User userExists = findUserByEmail(user.getEmail());

        if(userExists != null){
            return user.getPassword().equals(userExists.getPassword());
        }
        return false;
    }
}
