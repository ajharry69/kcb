package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.project.ProjectRepository;
import com.github.ajharry69.kcbtechnicalinterview.project.exceptions.ProjectNotFoundException;
import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import com.github.ajharry69.kcbtechnicalinterview.task.exceptions.DuplicateTaskException;
import com.github.ajharry69.kcbtechnicalinterview.task.exceptions.TaskNotFoundException;
import com.github.ajharry69.kcbtechnicalinterview.task.models.Task;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskRequest;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskResponse;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final ProjectRepository projectRepository;

    @Override
    public Page<TaskResponse> getTasks(UUID projectId,
                                       TaskStatus status,
                                       LocalDate dueDate,
                                       Pageable pageable) {
        Project project = getProjectByIdOrThrow(projectId);
        TaskSpecification specification = new TaskSpecification(project.getId(), status, dueDate);
        return repository.findAll(specification, pageable)
                .map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .name(task.getName())
                        .dueDate(task.getDueDate())
                        .status(task.getStatus())
                        .build());
    }

    @Override
    @Transactional
    public TaskResponse createTask(UUID projectId, TaskRequest task) {
        Project project = getProjectByIdOrThrow(projectId);

        if (repository.existsByNameIgnoreCase(task.name())) {
            throw new DuplicateTaskException();
        }
        Task entity = Task.builder()
                .name(task.name())
                .status(task.status())
                .dueDate(task.dueDate())
                .project(project)
                .build();
        Task saveTask = repository.save(entity);
        return TaskResponse.builder()
                .id(saveTask.getId())
                .name(saveTask.getName())
                .status(saveTask.getStatus())
                .dueDate(saveTask.getDueDate())
                .projectId(saveTask.getProject().getId())
                .build();
    }

    private Project getProjectByIdOrThrow(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

    @Override
    public TaskResponse getTask(UUID projectId, UUID taskId) {
        Task task = repository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .build();
    }

    @Override
    @Transactional
    public TaskResponse updateTask(UUID projectId, UUID taskId, TaskRequest task) {
        if (!repository.existsById(taskId)) {
            throw new TaskNotFoundException();
        }

        Task entity = Task.builder()
                .name(task.name())
                .status(task.status())
                .dueDate(task.dueDate())
                .build();
        Task saveTask = repository.save(entity);
        return TaskResponse.builder()
                .id(saveTask.getId())
                .name(saveTask.getName())
                .status(saveTask.getStatus())
                .dueDate(saveTask.getDueDate())
                .projectId(saveTask.getProject().getId())
                .build();
    }

    @Override
    @Transactional
    public void deleteTask(UUID projectId, UUID taskId) {
        if (!repository.existsById(taskId)) {
            throw new TaskNotFoundException();
        }

        repository.deleteById(taskId);
    }
}
