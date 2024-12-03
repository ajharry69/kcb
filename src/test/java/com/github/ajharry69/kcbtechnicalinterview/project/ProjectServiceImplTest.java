package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.exceptions.DuplicateProjectException;
import com.github.ajharry69.kcbtechnicalinterview.project.exceptions.ProjectNotFoundException;
import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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

class ProjectServiceImplTest {

    @Nested
    class GetProjects {
        @Test
        void shouldReturnEmpty_WhenProjectsAreNotAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            var service = new ProjectServiceImpl(repository);

            // When
            var actual = service.getProjects();

            // Then
            assertAll(
                    () -> verify(repository, times(1)).findAll(),
                    () -> assertIterableEquals(Collections.emptyList(), actual)
            );
        }

        @Test
        void shouldReturnNonEmpty_WhenProjectsAreAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.findAll())
                    .thenReturn(List.of(Project.builder().build()));
            var service = new ProjectServiceImpl(repository);

            // When
            var actual = service.getProjects();

            // Then
            assertAll(
                    () -> verify(repository, times(1)).findAll(),
                    () -> Assertions.assertThat(actual)
                            .isNotEmpty()
            );
        }
    }

    @Nested
    class GetProject {
        @Test
        void shouldThrowProjectNotFoundException_IfProjectIsNotAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.findById(any()))
                    .thenReturn(Optional.empty());
            var service = new ProjectServiceImpl(repository);

            // When
            assertThatThrownBy(() -> service.getProject(UUID.randomUUID()))
                    .isInstanceOf(ProjectNotFoundException.class);
        }

        @Test
        void shouldReturnProject_IfProjectIsAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.findById(any()))
                    .thenReturn(Optional.of(Project.builder().build()));
            var service = new ProjectServiceImpl(repository);

            // When
            ProjectResponse project = service.getProject(UUID.randomUUID());

            // Then
            assertThat(project)
                    .isNotNull();
        }
    }

    @Nested
    class DeleteProject {
        @Test
        void shouldThrowProjectNotFoundException_IfProjectIsNotAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.existsById(any()))
                    .thenReturn(false);
            var service = new ProjectServiceImpl(repository);

            // When
            assertAll(
                    () -> verify(repository, never()).save(any()),
                    () -> assertThatThrownBy(() -> service.deleteProject(UUID.randomUUID()))
                            .isInstanceOf(ProjectNotFoundException.class)
            );
        }

        @Test
        void shouldDelete_IfProjectIsAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.existsById(any()))
                    .thenReturn(true);
            var service = new ProjectServiceImpl(repository);

            // When
            UUID projectId = UUID.randomUUID();
            service.deleteProject(projectId);

            // Then
            var argumentCaptor = ArgumentCaptor.forClass(UUID.class);
            verify(repository, times(1)).deleteById(argumentCaptor.capture());

            var id = argumentCaptor.getValue();
            assertThat(id)
                    .isEqualTo(projectId);
        }
    }

    @Nested
    class UpdateProject {
        @Test
        void shouldThrowProjectNotFoundException_IfProjectIsNotAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.existsById(any()))
                    .thenReturn(false);
            var service = new ProjectServiceImpl(repository);

            // When
            assertAll(
                    () -> verify(repository, never()).save(any()),
                    () -> assertThatThrownBy(() -> service.updateProject(UUID.randomUUID(), ProjectRequest.builder().name("example").build()))
                            .isInstanceOf(ProjectNotFoundException.class)
            );
        }

        @Test
        void shouldReturnProject_WhenProjectsIsAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.existsById(any()))
                    .thenReturn(true);
            when(repository.save(any()))
                    .thenReturn(Project.builder().id(UUID.randomUUID()).build());
            var service = new ProjectServiceImpl(repository);

            // When
            var actual = service.updateProject(UUID.randomUUID(), ProjectRequest.builder().name("example").build());

            // Then
            assertAll(
                    () -> {
                        var argumentCaptor = ArgumentCaptor.forClass(Project.class);
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
    class CreateProject {
        @Test
        void shouldRaiseDuplicateProjectExceptionIfProjectExists() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.existsByNameIgnoreCase(anyString()))
                    .thenReturn(true);
            var service = new ProjectServiceImpl(repository);

            // When
            assertAll(
                    () -> verify(repository, never()).save(any()),
                    () -> assertThatThrownBy(() -> service.createProject(ProjectRequest.builder().name("example").build()))
                            .isInstanceOf(DuplicateProjectException.class)
            );
        }

        @Test
        void shouldReturnProject_WhenProjectsIsNotAvailable() {
            // Given
            var repository = mock(ProjectRepository.class);
            when(repository.save(any()))
                    .thenReturn(Project.builder().id(UUID.randomUUID()).build());
            var service = new ProjectServiceImpl(repository);

            // When
            var actual = service.createProject(ProjectRequest.builder().name("example").build());

            // Then
            assertAll(
                    () -> {
                        var argumentCaptor = ArgumentCaptor.forClass(Project.class);
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