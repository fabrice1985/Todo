package com.example.Todo.services;

import com.example.Todo.entities.User;
import com.example.Todo.exceptions.TodoNotFoundException;
import com.example.Todo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public User findById (Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id = " + id));
    }

}
