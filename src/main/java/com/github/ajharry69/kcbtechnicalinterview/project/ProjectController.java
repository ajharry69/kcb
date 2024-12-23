package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectSummaryResponse;
import com.github.ajharry69.kcbtechnicalinterview.project.utils.ProjectAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/projects", produces = {MediaTypes.HAL_JSON_VALUE})
public class ProjectController {
    private final ProjectService service;
    private final PagedResourcesAssembler<ProjectResponse> projectPageAssembler;

    @GetMapping
    public PagedModel<EntityModel<ProjectResponse>> getProjects(Pageable pageable) {
        Page<ProjectResponse> projects = service.getProjects(pageable);
        return projectPageAssembler.toModel(
                projects,
                new ProjectAssembler()
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProjectResponse>> createProject(@RequestBody @Valid ProjectRequest project) {
        ProjectResponse response = service.createProject(project);
        ProjectAssembler assembler = new ProjectAssembler();
        EntityModel<ProjectResponse> model = assembler.toModel(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<EntityModel<ProjectResponse>> getProject(
            @PathVariable
            UUID projectId) {
        ProjectResponse response = service.getProject(projectId);
        ProjectAssembler assembler = new ProjectAssembler();
        EntityModel<ProjectResponse> model = assembler.toModel(response);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<EntityModel<ProjectResponse>> updateProject(
            @PathVariable
            UUID projectId,
            @RequestBody @Valid ProjectRequest project) {
        ProjectResponse response = service.updateProject(projectId, project);
        ProjectAssembler assembler = new ProjectAssembler();
        EntityModel<ProjectResponse> model = assembler.toModel(response);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(
            @PathVariable
            UUID projectId) {
        service.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ProjectSummaryResponse>> getProjectSummary() {
        List<ProjectSummaryResponse> summary = service.getProjectSummary();
        return ResponseEntity.ok(summary);
    }
}
