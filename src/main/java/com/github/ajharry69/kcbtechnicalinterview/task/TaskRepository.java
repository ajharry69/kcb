package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.task.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
    Boolean existsByNameIgnoreCase(String name);
}
