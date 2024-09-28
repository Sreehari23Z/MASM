package com.example.MASM.Controller;

import com.example.MASM.model.User;
import com.example.MASM.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("/users/{username}/{password}")
    ResponseEntity<String> userLogin(@PathVariable String username, @PathVariable String password){
        Optional<User>userOptional=userRepository.findById(username);
        if(userOptional.isPresent()){
            if(userOptional.get().getUser_name().equals(username)){
                if(userOptional.get().getPassword().equals(password)){
                    return ResponseEntity.ok("Logged in as "+userOptional.get().getFname()+" "+userOptional.get().getLname());
                }else{
                    return ResponseEntity.badRequest().body("Password is incorrect");
                }
            }else{
                return ResponseEntity.badRequest().body("No such user found");
            }
        }
        return ResponseEntity.badRequest().body("No such user found");
    }

    @GetMapping("/users")
    ResponseEntity<User>message(){
        User user = new User("abc","abc","abc","abc");
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/register")
    ResponseEntity<String>userRegister(@RequestBody User user){
        Optional<User>userOptional=userRepository.findById(user.getUser_name());
        if(userOptional.isPresent()){
            return ResponseEntity.badRequest().body("User Already Exist");
        }userRepository.save(user);
        return ResponseEntity.ok("Logged in as "+user.getFname()+" "+user.getLname());
    }

}
