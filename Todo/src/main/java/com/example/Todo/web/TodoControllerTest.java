package com.example.Todo.web;

import com.example.Todo.entities.Todo;
import com.example.Todo.services.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // on me demande d'ajouter la dependance dans maven pourtant s'est deja fait 
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoControllerTest.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean    //add maven dependency
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_shouldReturn200AndList() throws Exception {
        // Arrange
        Todo t1 = new Todo(1L, "Task 1", "Desc 1");
        Todo t2 = new Todo(2L, "Task 2", "Desc 2");
        given(todoService.findAll()).willReturn(List.of(t1, t2));

        // Act + Assert
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titre").value("Task 1"));

        verify(todoService).findAll();
    }

    @Test
    void getById_shouldReturn200AndTodo() throws Exception {
        // Arrange
        Todo t = new Todo(1L, "Task 1", "Desc 1");
        given(todoService.findById(1L)).willReturn(t);

        // Act + Assert
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titre").value("Task 1"));

        verify(todoService).findById(1L);
    }

    @Test
    void create_shouldReturn201AndBody() throws Exception {
        // Arrange
        Todo toCreate = new Todo(null, "New task", "New desc");
        Todo created = new Todo(1L, "New task", "New desc");
        given(todoService.create(toCreate)).willReturn(created);

        // Act + Assert
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titre").value("New task"));

        verify(todoService).create(toCreate);
    }

    @Test
    void update_shouldReturn200AndUpdatedTodo() throws Exception {
        // Arrange
        Todo updates = new Todo(null, "Updated title", "Updated desc");
        Todo updated = new Todo(1L, "Updated title", "Updated desc");
        given(todoService.update(1L, updates)).willReturn(updated);

        // Act + Assert
        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titre").value("Updated title"));

        verify(todoService).update(1L, updates);
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        // Act + Assert
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());

        verify(todoService).delete(1L);
    }
}
