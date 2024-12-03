package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.exceptions.DuplicateProjectException;
import com.github.ajharry69.kcbtechnicalinterview.project.exceptions.ProjectNotFoundException;
import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectSummaryResponse;
import com.github.ajharry69.kcbtechnicalinterview.task.models.Task;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;

    @Override
    public List<ProjectResponse> getProjects() {
        return repository.findAll()
                .stream()
                .map(project -> ProjectResponse.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .description(project.getDescription())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest project) {
        if (repository.existsByNameIgnoreCase(project.name())) {
            throw new DuplicateProjectException();
        }
        Project entity = Project.builder()
                .name(project.name())
                .description(project.description())
                .build();
        Project saveProject = repository.save(entity);
        return ProjectResponse.builder()
                .id(saveProject.getId())
                .name(saveProject.getName())
                .description(saveProject.getDescription())
                .build();
    }

    @Override
    public ProjectResponse getProject(UUID projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .build();
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(UUID projectId, ProjectRequest project) {
        if (!repository.existsById(projectId)) {
            throw new ProjectNotFoundException();
        }

        Project entity = Project.builder()
                .name(project.name())
                .description(project.description())
                .build();
        Project saveProject = repository.save(entity);
        return ProjectResponse.builder()
                .id(saveProject.getId())
                .name(saveProject.getName())
                .description(saveProject.getDescription())
                .build();
    }

    @Override
    @Transactional
    public void deleteProject(UUID projectId) {
        if (!repository.existsById(projectId)) {
            throw new ProjectNotFoundException();
        }

        repository.deleteById(projectId);
    }

    @Override
    public List<ProjectSummaryResponse> getProjectSummary() {
        List<Project> projects = repository.findAll();
        List<ProjectSummaryResponse> summaries = new ArrayList<>();

        for (Project project : projects) {
            Map<TaskStatus, Long> taskCountByStatus = project.getTasks().stream()
                    .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

            ProjectSummaryResponse response = new ProjectSummaryResponse();
            response.setProjectId(project.getId());
            response.setProjectName(project.getName());
            response.setTaskCountByStatus(taskCountByStatus);
            summaries.add(response);
        }

        return summaries;
    }
}
