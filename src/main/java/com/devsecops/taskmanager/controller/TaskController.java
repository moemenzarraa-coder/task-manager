package com.devsecops.taskmanager.controller;

import com.devsecops.taskmanager.model.Task;
import com.devsecops.taskmanager.model.TaskStatus;
import com.devsecops.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getAll(@RequestParam(required = false) TaskStatus status) {
        return status == null ? service.findAll() : service.findByStatus(status);
    }

    @GetMapping("/{id}")
    public Task getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        Task created = service.create(task);
        return ResponseEntity.created(URI.create("/api/tasks/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody Task task) {
        return service.update(id, task);
    }

    @PatchMapping("/{id}/status")
    public Task updateStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
