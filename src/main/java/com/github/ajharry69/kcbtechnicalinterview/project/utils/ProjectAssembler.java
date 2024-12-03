package com.github.ajharry69.kcbtechnicalinterview.project.utils;

import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class ProjectAssembler implements RepresentationModelAssembler<ProjectResponse, EntityModel<ProjectResponse>> {
    @Override
    public EntityModel<ProjectResponse> toModel(ProjectResponse entity) {
        return EntityModel.of(
                entity
        );
    }
}