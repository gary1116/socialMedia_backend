package com.social.demo.controllers;

import com.social.demo.models.SocialUser;
import com.social.demo.services.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SocialController {

    @Autowired
    private SocialService socialService;


    @GetMapping("/social/users")
    public ResponseEntity<List<SocialUser>> getUsers(){
        return new ResponseEntity<>(socialService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/social/adduser")
    public ResponseEntity<SocialUser> addUser(@RequestBody SocialUser socialUser){
        return new ResponseEntity<>(socialService.saveUser(socialUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/social/user/{userId}")
    public ResponseEntity<String> deleteUsers(@PathVariable Long userId){
        socialService.delete(userId);
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }

}
