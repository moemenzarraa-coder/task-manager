package com.devsecops.taskmanager.controller;

import com.devsecops.taskmanager.exception.TaskNotFoundException;
import com.devsecops.taskmanager.model.Task;
import com.devsecops.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TaskService service;

    @Test
    void getAllReturnsList() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Task("A", null)));

        mvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("A"));
    }

    @Test
    void createReturns201() throws Exception {
        Task saved = new Task("Nouvelle tache", "desc");
        saved.setId(1L);
        when(service.create(any(Task.class))).thenReturn(saved);

        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Nouvelle tache\",\"description\":\"desc\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tasks/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createRejectsBlankTitle() throws Exception {
        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOneReturns404WhenMissing() throws Exception {
        when(service.findById(99L)).thenThrow(new TaskNotFoundException(99L));

        mvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns204() throws Exception {
        mvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
