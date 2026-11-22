package com.devsecops.taskmanager.service;

import com.devsecops.taskmanager.exception.TaskNotFoundException;
import com.devsecops.taskmanager.model.Task;
import com.devsecops.taskmanager.model.TaskStatus;
import com.devsecops.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    void createSetsDefaultStatus() {
        Task task = new Task("Ecrire le Dockerfile", null);
        task.setStatus(null);
        when(repository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task saved = service.create(task);

        assertThat(saved.getStatus()).isEqualTo(TaskStatus.TODO);
        verify(repository).save(task);
    }

    @Test
    void findAllReturnsTasks() {
        when(repository.findAll()).thenReturn(List.of(new Task("A", null), new Task("B", null)));

        assertThat(service.findAll()).hasSize(2);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(42L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateChangesFields() {
        Task existing = new Task("Old", "old desc");
        existing.setId(1L);
        Task data = new Task("New", "new desc");
        data.setStatus(TaskStatus.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task updated = service.update(1L, data);

        assertThat(updated.getTitle()).isEqualTo("New");
        assertThat(updated.getDescription()).isEqualTo("new desc");
        assertThat(updated.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void updateStatusMarksDone() {
        Task existing = new Task("T", null);
        existing.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThat(service.updateStatus(1L, TaskStatus.DONE).getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void deleteRemovesExistingTask() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(repository.existsById(9L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(9L)).isInstanceOf(TaskNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }
}
