package com.apkharsh.inventory.repositories;

import com.apkharsh.inventory.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {

    @Query("{ 'email' : ?0 }")
    User findByEmail(String email);
}
