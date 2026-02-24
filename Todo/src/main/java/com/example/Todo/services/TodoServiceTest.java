package com.example.Todo.services;

import com.example.Todo.entities.Todo;
import com.example.Todo.repositories.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void findAll_shouldReturnListOfTodos() {
        // Arrange
        Todo t1 = new Todo(1L, "Task 1", "Description 1");
        Todo t2 = new Todo(2L, "Task 2", "Description 2");
        given(todoRepository.findAll()).willReturn(List.of(t1, t2));

        // Act
        List<Todo> result = todoService.findAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitre()).isEqualTo("Task 1");
        verify(todoRepository).findAll();
    }

    @Test
    void findById_whenFound_shouldReturnTodo() {
        // Arrange
        Todo t = new Todo(1L, "Task 1", "Description 1");
        given(todoRepository.findById(1L)).willReturn(Optional.of(t));

        // Act
        Todo result = todoService.findById(1L);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitre()).isEqualTo("Task 1");
        verify(todoRepository).findById(1L);
    }

    @Test
    void findById_whenNotFound_shouldThrowException() {
        // Arrange
        given(todoRepository.findById(99L)).willReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> todoService.findById(99L));
        verify(todoRepository).findById(99L);
    }

    @Test
    void create_shouldSaveAndReturnTodo() {
        // Arrange
        Todo toCreate = new Todo(null, "New task", "New description");
        Todo saved = new Todo(1L, "New task", "New description");
        given(todoRepository.save(toCreate)).willReturn(saved);

        // Act
        Todo result = todoService.create(toCreate);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitre()).isEqualTo("New task");
        verify(todoRepository).save(toCreate);
    }

    @Test
    void update_shouldModifyExistingTodo() {
        // Arrange
        Todo existing = new Todo(1L, "Old title", "Old desc");
        Todo updates = new Todo(null, "New title", "New desc");

        given(todoRepository.findById(1L)).willReturn(Optional.of(existing));
        given(todoRepository.save(existing)).willReturn(existing);

        // Act
        Todo result = todoService.update(1L, updates);

        // Assert
        assertThat(result.getTitre()).isEqualTo("New title");
        assertThat(result.getDescription()).isEqualTo("New desc");
        verify(todoRepository).findById(1L);
        verify(todoRepository).save(existing);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        // Act
        todoService.delete(1L);

        // Assert
        verify(todoRepository).deleteById(1L);
    }
}
