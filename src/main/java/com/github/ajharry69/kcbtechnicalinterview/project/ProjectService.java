package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    List<ProjectResponse> getProjects();

    ProjectResponse createProject(ProjectRequest project);

    ProjectResponse getProject(UUID projectId);
    ProjectResponse updateProject(UUID projectId, ProjectRequest project);

    void deleteProject(UUID projectId);
}
