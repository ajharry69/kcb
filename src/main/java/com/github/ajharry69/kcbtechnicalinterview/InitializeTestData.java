package com.github.ajharry69.kcbtechnicalinterview;


import com.github.ajharry69.kcbtechnicalinterview.project.ProjectRepository;
import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import com.github.ajharry69.kcbtechnicalinterview.task.TaskRepository;
import com.github.ajharry69.kcbtechnicalinterview.task.models.Task;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class InitializeTestData implements CommandLineRunner {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void run(String... args) {
        projectRepository.deleteAll();
        for (int i = 1; i <= 10; i++) {
            Project project = Project.builder()
                    .name("Project " + i)
                    .build();
            project = projectRepository.save(project);

            for (int j = 0; j < new Random().nextInt(5); j++) {
                int daysToAddOrSubtract = new Random().nextInt(1, 10);

                boolean addDays = new Random().nextBoolean();

                LocalDate dueDate = addDays
                        ? LocalDate.now().plusDays(daysToAddOrSubtract)
                        : LocalDate.now().minusDays(daysToAddOrSubtract);

                int randomTaskPosition = new Random().nextInt(TaskStatus.values().length);
                TaskStatus status = TaskStatus.values()[randomTaskPosition];

                Task task = Task.builder()
                        .project(project)
                        .name("Task " + j)
                        .dueDate(dueDate)
                        .status(status)
                        .build();
                taskRepository.save(task);
            }
        }
    }
}
