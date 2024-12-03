package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.project.ProjectRepository;
import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import com.github.ajharry69.kcbtechnicalinterview.task.exceptions.DuplicateTaskException;
import com.github.ajharry69.kcbtechnicalinterview.task.exceptions.TaskNotFoundException;
import com.github.ajharry69.kcbtechnicalinterview.task.models.Task;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskRequest;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Nested
    class GetTasks {
        @Test
        void shouldReturnEmpty_WhenTasksAreNotAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            var actual = service.getTasks(UUID.randomUUID(), null, null, Pageable.unpaged());

            // Then
            assertAll(
                    () -> verify(repository, times(1)).findAll(),
                    () -> assertIterableEquals(Collections.emptyList(), actual)
            );
        }

        @Test
        void shouldReturnNonEmpty_WhenTasksAreAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            var specification = mock(TaskSpecification.class);
            when(repository.findAll(specification, any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(Task.builder().build())));
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            var actual = service.getTasks(UUID.randomUUID(), null, null, Pageable.unpaged());

            // Then
            assertAll(
                    () -> verify(repository, times(1)).findAll(),
                    () -> Assertions.assertThat(actual)
                            .isNotEmpty()
            );
        }
    }

    @Nested
    class GetTask {
        @Test
        void shouldThrowTaskNotFoundException_IfTaskIsNotAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            when(repository.findById(any()))
                    .thenReturn(Optional.empty());
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            assertThatThrownBy(() -> service.getTask(UUID.randomUUID(), UUID.randomUUID()))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        void shouldReturnTask_IfTaskIsAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            when(repository.findById(any()))
                    .thenReturn(Optional.of(Task.builder().build()));
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            TaskResponse task = service.getTask(UUID.randomUUID(), UUID.randomUUID());

            // Then
            assertThat(task)
                    .isNotNull();
        }
    }

    @Nested
    class DeleteTask {
        @Test
        void shouldThrowTaskNotFoundException_IfTaskIsNotAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            when(repository.existsById(any()))
                    .thenReturn(false);
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            assertAll(
                    () -> verify(repository, never()).save(any()),
                    () -> assertThatThrownBy(() -> service.deleteTask(UUID.randomUUID(), UUID.randomUUID()))
                            .isInstanceOf(TaskNotFoundException.class)
            );
        }

        @Test
        void shouldDelete_IfTaskIsAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            when(repository.existsById(any()))
                    .thenReturn(true);
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            UUID taskId = UUID.randomUUID();
            service.deleteTask(UUID.randomUUID(), taskId);

            // Then
            var argumentCaptor = ArgumentCaptor.forClass(UUID.class);
            verify(repository, times(1)).deleteById(argumentCaptor.capture());

            var id = argumentCaptor.getValue();
            assertThat(id)
                    .isEqualTo(taskId);
        }
    }

    @Nested
    class UpdateTask {
        @Test
        void shouldThrowTaskNotFoundException_IfTaskIsNotAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            when(repository.existsById(any()))
                    .thenReturn(false);
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            assertAll(
                    () -> verify(repository, never()).save(any()),
                    () -> assertThatThrownBy(() -> service.updateTask(UUID.randomUUID(), UUID.randomUUID(), TaskRequest.builder().name("example").build()))
                            .isInstanceOf(TaskNotFoundException.class)
            );
        }

        @Test
        void shouldReturnTask_WhenTasksIsAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            when(repository.existsById(any()))
                    .thenReturn(true);
            Project project = Project.builder().build();
            Task task = Task.builder().id(UUID.randomUUID()).project(project).build();
            when(repository.save(any()))
                    .thenReturn(task);
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            var actual = service.updateTask(UUID.randomUUID(), UUID.randomUUID(), TaskRequest.builder().name("example").build());

            // Then
            assertAll(
                    () -> {
                        var argumentCaptor = ArgumentCaptor.forClass(Task.class);
                        verify(repository, times(1)).save(argumentCaptor.capture());

                        var entity = argumentCaptor.getValue();
                        assertThat(entity.getId())
                                .isNull();
                        assertThat(entity.getName())
                                .isEqualTo("example");
                    },
                    () -> assertThat(actual.id())
                            .isNotNull()
            );
        }
    }

    @Nested
    class CreateTask {
        @Test
        void shouldRaiseDuplicateTaskExceptionIfTaskExists() {
            // Given
            var repository = mock(TaskRepository.class);
            when(repository.existsByNameIgnoreCase(anyString()))
                    .thenReturn(true);
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            assertAll(
                    () -> verify(repository, never()).save(any()),
                    () -> assertThatThrownBy(() -> service.createTask(UUID.randomUUID(), TaskRequest.builder().name("example").build()))
                            .isInstanceOf(DuplicateTaskException.class)
            );
        }

        @Test
        void shouldReturnTask_WhenTasksIsNotAvailable() {
            // Given
            var repository = mock(TaskRepository.class);
            Project project = Project.builder().build();
            Task task = Task.builder().id(UUID.randomUUID()).project(project).build();
            when(repository.save(any()))
                    .thenReturn(task);
            var projectRepository = mock(ProjectRepository.class);
            when(projectRepository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new TaskServiceImpl(repository, projectRepository);

            // When
            var actual = service.createTask(UUID.randomUUID(), TaskRequest.builder().name("example").build());

            // Then
            assertAll(
                    () -> {
                        var argumentCaptor = ArgumentCaptor.forClass(Task.class);
                        verify(repository, times(1)).save(argumentCaptor.capture());

                        var entity = argumentCaptor.getValue();
                        assertThat(entity.getId())
                                .isNull();
                        assertThat(entity.getName())
                                .isEqualTo("example");
                    },
                    () -> assertThat(actual.id())
                            .isNotNull()
            );
        }
    }
}