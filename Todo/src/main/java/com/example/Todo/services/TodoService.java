package com.example.Todo.services;

import com.example.Todo.entities.Todo;
import com.example.Todo.repositories.TodoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public List<Todo> findAll() {
        return repo.findAll();
    }

    public Todo findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo non trouvé"));
    }

    public Todo create(Todo todo) {
        return repo.save(todo);
    }

    public Todo update(Long id, Todo todo) {
        Todo existing = findById(id);
        existing.setTitre(todo.getTitre());
        existing.setDescription(todo.getDescription());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
