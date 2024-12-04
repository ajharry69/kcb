package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    Page<ProjectResponse> getProjects(Pageable pageable);

    ProjectResponse createProject(ProjectRequest project);

    ProjectResponse getProject(UUID projectId);

    ProjectResponse updateProject(UUID projectId, ProjectRequest project);

    void deleteProject(UUID projectId);

    List<ProjectSummaryResponse> getProjectSummary();
}
