package com.example.MASM.repository;

import com.example.MASM.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserRepository extends MongoRepository<User,String> {

}
