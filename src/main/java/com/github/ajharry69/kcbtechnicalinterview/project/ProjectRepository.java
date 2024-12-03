package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Boolean existsByNameIgnoreCase(String name);
}
