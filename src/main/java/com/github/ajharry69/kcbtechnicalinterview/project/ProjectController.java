package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import com.github.ajharry69.kcbtechnicalinterview.project.utils.ProjectAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService service;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects() {
        List<ProjectResponse> projects = service.getProjects();
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProjectResponse>> createProject(@RequestBody @Valid ProjectRequest project) {
        ProjectResponse response = service.createProject(project);
        ProjectAssembler assembler = new ProjectAssembler();
        EntityModel<ProjectResponse> model = assembler.toModel(response);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<EntityModel<ProjectResponse>> getProject(
            @PathVariable
            UUID projectId) {
        ProjectResponse response = service.getProject(projectId);
        ProjectAssembler assembler = new ProjectAssembler();
        EntityModel<ProjectResponse> model = assembler.toModel(response);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<EntityModel<ProjectResponse>> updateProject(
            @PathVariable
            UUID projectId,
            @RequestBody @Valid ProjectRequest project) {
        ProjectResponse response = service.updateProject(projectId, project);
        ProjectAssembler assembler = new ProjectAssembler();
        EntityModel<ProjectResponse> model = assembler.toModel(response);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(
            @PathVariable
            UUID projectId) {
        service.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
