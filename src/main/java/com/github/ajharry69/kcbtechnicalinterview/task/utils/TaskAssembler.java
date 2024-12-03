package com.github.ajharry69.kcbtechnicalinterview.task.utils;

import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class TaskAssembler implements RepresentationModelAssembler<TaskResponse, EntityModel<TaskResponse>> {
    @Override
    public EntityModel<TaskResponse> toModel(TaskResponse entity) {
        return EntityModel.of(
                entity
        );
    }
}