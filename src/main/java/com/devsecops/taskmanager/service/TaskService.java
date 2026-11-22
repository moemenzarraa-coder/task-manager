package com.devsecops.taskmanager.service;

import com.devsecops.taskmanager.exception.TaskNotFoundException;
import com.devsecops.taskmanager.model.Task;
import com.devsecops.taskmanager.model.TaskStatus;
import com.devsecops.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public List<Task> findByStatus(TaskStatus status) {
        return repository.findByStatus(status);
    }

    public Task findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task create(Task task) {
        task.setId(null);
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        return repository.save(task);
    }

    public Task update(Long id, Task data) {
        Task task = findById(id);
        task.setTitle(data.getTitle());
        task.setDescription(data.getDescription());
        task.setDueDate(data.getDueDate());
        if (data.getStatus() != null) {
            task.setStatus(data.getStatus());
        }
        return repository.save(task);
    }

    public Task updateStatus(Long id, TaskStatus status) {
        Task task = findById(id);
        task.setStatus(status);
        return repository.save(task);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
