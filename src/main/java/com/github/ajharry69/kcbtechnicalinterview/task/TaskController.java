package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskRequest;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskResponse;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import com.github.ajharry69.kcbtechnicalinterview.task.utils.TaskAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class TaskController {
    private final TaskService service;
    private final PagedResourcesAssembler<TaskResponse> taskPageAssembler;

    @GetMapping("/{projectId}/tasks")
    public PagedModel<EntityModel<TaskResponse>> getTasks(
            @PathVariable
            UUID projectId,
            @RequestParam(required = false)
            TaskStatus status,
            @RequestParam(required = false)
            LocalDate dueDate,
            Pageable pageable) {
        Page<TaskResponse> tasks = service.getTasks(projectId, status, dueDate, pageable);
        return taskPageAssembler.toModel(
                tasks,
                new TaskAssembler()
        );
    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<EntityModel<TaskResponse>> createTask(
            @PathVariable
            UUID projectId,
            @RequestBody @Valid TaskRequest task) {
        TaskResponse response = service.createTask(projectId, task);
        TaskAssembler assembler = new TaskAssembler();
        EntityModel<TaskResponse> model = assembler.toModel(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<EntityModel<TaskResponse>> getTask(
            @PathVariable
            UUID projectId,
            @PathVariable
            UUID taskId) {
        TaskResponse response = service.getTask(projectId, taskId);
        TaskAssembler assembler = new TaskAssembler();
        EntityModel<TaskResponse> model = assembler.toModel(response);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<EntityModel<TaskResponse>> updateTask(
            @PathVariable
            UUID projectId,
            @PathVariable
            UUID taskId,
            @RequestBody @Valid TaskRequest task) {
        TaskResponse response = service.updateTask(projectId, taskId, task);
        TaskAssembler assembler = new TaskAssembler();
        EntityModel<TaskResponse> model = assembler.toModel(response);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable
            UUID projectId,
            @PathVariable
            UUID taskId) {
        service.deleteTask(projectId, taskId);
        return ResponseEntity.noContent().build();
    }
}
