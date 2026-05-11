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

    private static final Long TODO_ID = 1L;
    private static final Long ANOTHER_TODO_ID = 2L;
    private static final Long UNKNOWN_TODO_ID = 99L;

    private static final String TODO_TITLE = "task1";
    private static final String TODO_DESCRIPTION = "Description 1";

    private static final String SECOND_TODO_TITLE = "Task 2";
    private static final String SECOND_TODO_DESCRIPTION = "Description 2";

    private static final String NEW_TITLE = "New task";
    private static final String NEW_DESCRIPTION = "New description";

    private static final String OLD_TITLE = "Old title";
    private static final String OLD_DESCRIPTION = "Old desc";

    private static final String UPDATED_TITLE = "New title";
    private static final String UPDATED_DESCRIPTION = "New desc";

    @Test
    void findAll_shouldReturnListOfTodos() {
        Todo t1 = new Todo(TODO_ID, TODO_TITLE, TODO_DESCRIPTION);
        Todo t2 = new Todo(ANOTHER_TODO_ID, SECOND_TODO_TITLE, SECOND_TODO_DESCRIPTION);
        given(todoRepository.findAll()).willReturn(List.of(t1, t2));

        List<Todo> result = todoService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitre()).isEqualTo(TODO_TITLE);
        verify(todoRepository).findAll();
    }

    @Test
    void findById_whenFound_shouldReturnTodo() {
        Todo t = new Todo(TODO_ID, TODO_TITLE, TODO_DESCRIPTION);
        given(todoRepository.findById(TODO_ID)).willReturn(Optional.of(t));

        Todo result = todoService.findById(TODO_ID);

        assertThat(result.getId()).isEqualTo(TODO_ID);
        assertThat(result.getTitre()).isEqualTo(TODO_TITLE);
        verify(todoRepository).findById(TODO_ID);
    }

    @Test
    void findById_whenNotFound_shouldThrowException() {
        given(todoRepository.findById(UNKNOWN_TODO_ID)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> todoService.findById(UNKNOWN_TODO_ID));
        verify(todoRepository).findById(UNKNOWN_TODO_ID);
    }

    @Test
    void create_shouldSaveAndReturnTodo() {
        Todo toCreate = new Todo(null, NEW_TITLE, NEW_DESCRIPTION);
        Todo saved = new Todo(TODO_ID, NEW_TITLE, NEW_DESCRIPTION);
        given(todoRepository.save(toCreate)).willReturn(saved);

        Todo result = todoService.create(toCreate);

        assertThat(result.getId()).isEqualTo(TODO_ID);
        assertThat(result.getTitre()).isEqualTo(NEW_TITLE);
        verify(todoRepository).save(toCreate);
    }

    @Test
    void update_shouldModifyExistingTodo() {
        Todo existing = new Todo(TODO_ID, OLD_TITLE, OLD_DESCRIPTION);
        Todo updates = new Todo(null, UPDATED_TITLE, UPDATED_DESCRIPTION);

        given(todoRepository.findById(TODO_ID)).willReturn(Optional.of(existing));
        given(todoRepository.save(existing)).willReturn(existing);

        Todo result = todoService.update(TODO_ID, updates);

        assertThat(result.getTitre()).isEqualTo(UPDATED_TITLE);
        assertThat(result.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        verify(todoRepository).findById(TODO_ID);
        verify(todoRepository).save(existing);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        todoService.delete(TODO_ID);

        verify(todoRepository).deleteById(TODO_ID);
    }
}
