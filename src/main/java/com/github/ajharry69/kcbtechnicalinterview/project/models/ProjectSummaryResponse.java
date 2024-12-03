package com.github.ajharry69.kcbtechnicalinterview.project.models;

import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSummaryResponse {
    private UUID projectId;
    private String projectName;
    private Map<TaskStatus, Long> taskCountByStatus;
}