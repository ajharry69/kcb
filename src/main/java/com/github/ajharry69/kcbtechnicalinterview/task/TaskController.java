package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskRequest;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskResponse;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import com.github.ajharry69.kcbtechnicalinterview.task.utils.TaskAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class TaskController {
    private final TaskService service;

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable
            UUID projectId,
            @RequestParam(required = false)
            TaskStatus status,
            @RequestParam(required = false)
            LocalDate startDate,
            Pageable pageable) {
        List<TaskResponse> tasks = service.getTasks(projectId, status, startDate, pageable);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<EntityModel<TaskResponse>> createTask(
            @PathVariable
            UUID projectId,
            @RequestBody @Valid TaskRequest task) {
        TaskResponse response = service.createTask(projectId, task);
        TaskAssembler assembler = new TaskAssembler();
        EntityModel<TaskResponse> model = assembler.toModel(response);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
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
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
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
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
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
