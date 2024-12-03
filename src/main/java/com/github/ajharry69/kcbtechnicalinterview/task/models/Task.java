package com.github.ajharry69.kcbtechnicalinterview.task.models;

import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Column(nullable = false)
    private LocalDate dueDate;
    @ManyToOne
    private Project project;
}
