package com.example.Todo.web;

import com.example.Todo.entities.Todo;
import com.example.Todo.services.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean; // on me demande d'ajouter la dependance dans maven pourtant s'est deja fait
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean    //add maven dependency
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String API_TODOS = "/api/todos";

    private static final Long TODO_ID = 1L;
    private static final Long SECOND_TODO_ID = 2L;

    private static final String TASK_1 = "Task 1";
    private static final String DESC_1 = "Desc 1";

    private static final String TASK_2 = "Task 2";
    private static final String DESC_2 = "Desc 2";

    private static final String NEW_TASK = "New task";
    private static final String NEW_DESC = "New desc";

    private static final String UPDATED_TITLE = "Updated title";
    private static final String UPDATED_DESC = "Updated desc";


    @Test
    void getAll_shouldReturn200AndList() throws Exception {
        Todo t1 = new Todo(TODO_ID, TASK_1, DESC_1);
        Todo t2 = new Todo(SECOND_TODO_ID, TASK_2, DESC_2);
        given(todoService.findAll()).willReturn(List.of(t1, t2));

        mockMvc.perform(get(API_TODOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titre").value(TASK_1));

        verify(todoService).findAll();
    }

    @Test
    void getById_shouldReturn200AndTodo() throws Exception {
        Todo todo = new Todo(TODO_ID, TASK_1, DESC_1);
        given(todoService.findById(TODO_ID)).willReturn(todo);

        mockMvc.perform(get(API_TODOS + "/" + TODO_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TODO_ID))
                .andExpect(jsonPath("$.titre").value(TASK_1));

        verify(todoService).findById(TODO_ID);
    }

    @Test
    void create_shouldReturn201AndBody() throws Exception {
        Todo toCreate = new Todo(null, NEW_TASK, NEW_DESC);
        Todo created = new Todo(TODO_ID, NEW_TASK, NEW_DESC);
        given(todoService.create(toCreate)).willReturn(created);

        mockMvc.perform(post(API_TODOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TODO_ID))
                .andExpect(jsonPath("$.titre").value(NEW_TASK));

        verify(todoService).create(toCreate);
    }

    @Test
    void update_shouldReturn200AndUpdatedTodo() throws Exception {
        Todo updates = new Todo(null, UPDATED_TITLE, UPDATED_DESC);
        Todo updated = new Todo(TODO_ID, UPDATED_TITLE, UPDATED_DESC);
        given(todoService.update(TODO_ID, updates)).willReturn(updated);

        mockMvc.perform(put(API_TODOS + "/" + TODO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TODO_ID))
                .andExpect(jsonPath("$.titre").value(UPDATED_TITLE));

        verify(todoService).update(TODO_ID, updates);
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete(API_TODOS + "/" + TODO_ID))
                .andExpect(status().isNoContent());

        verify(todoService).delete(TODO_ID);
    }
}
