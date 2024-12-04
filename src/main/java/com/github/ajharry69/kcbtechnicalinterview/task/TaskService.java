package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskRequest;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskResponse;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    Page<TaskResponse> getTasks(UUID projectId,
                                TaskStatus status,
                                LocalDate dueDate,
                                Pageable pageable);

    TaskResponse createTask(UUID projectId, TaskRequest task);

    TaskResponse getTask(UUID projectId, UUID taskId);

    TaskResponse updateTask(UUID projectId, UUID taskId, TaskRequest task);

    void deleteTask(UUID projectId, UUID taskId);
}
