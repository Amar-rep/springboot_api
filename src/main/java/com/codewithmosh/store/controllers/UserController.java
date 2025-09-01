package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor

public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @GetMapping("/users")
    public Iterable<UserDto> getAllUsers(@RequestHeader(required = false, name="x-auth_token") String authToken, @RequestParam(required = false,defaultValue = "",name="sort")  String sort) {
        System.out.println(authToken);
        if(!Set.of("name","email").contains(sort))
        {
            sort="name";
        }
        List<User> users  = userRepository.findAll(Sort.by(sort).descending());
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i).getName());
        }

        return users.stream().map( user->userMapper.toDto(user)).toList();
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable  Long id)
    {
        var user= userRepository.findById(id).orElse(null);
        if(user==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }
    // we need to handle exeption handler

    @PostMapping("/user/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody RegisterUserRequest registerUserRequest, UriComponentsBuilder uriComponentsBuilder)
    {  var check= userRepository.existsByEmail(registerUserRequest.getEmail());
        System.out.println(registerUserRequest);
        var user=userMapper.toEntity(registerUserRequest);
        userRepository.save(user);
        var userDto=userMapper.toDto(user);


        return ResponseEntity.ok(userDto);

    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
    {
        var user=userRepository.findById(id).orElse(null);
        if(user==null)
        {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }


}
