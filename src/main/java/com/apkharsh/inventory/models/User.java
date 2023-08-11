package com.apkharsh.inventory.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
public class User {
    @Id
    private String ID;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String role;
    public User() {
        super();
    }

    public User(String email, String password) {
        super();
        this.ID = java.util.UUID.randomUUID().toString(); // for random id generation
        this.email = email;
        this.password = password;
        this.role = "user";
    }

    public User(String ID, String email, String password) {
        super();
        this.ID = ID;
        this.email = email;
        this.password = password;
        this.role = "user";
    }

    public String getID() {
        return ID;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() { return role; }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", password=" + password + "]";
    }

}