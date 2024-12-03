package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.exceptions.DuplicateProjectException;
import com.github.ajharry69.kcbtechnicalinterview.project.exceptions.ProjectNotFoundException;
import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
}
