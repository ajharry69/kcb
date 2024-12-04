package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.project.ProjectRepository;
import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import com.github.ajharry69.kcbtechnicalinterview.task.models.Task;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {
    private Project project;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = RestAssured.DEFAULT_PORT;

        projectRepository.deleteAll();
        taskRepository.deleteAll();

        project = projectRepository.save(
                Project.builder()
                        .name("Example")
                        .build()
        );
    }

    @Nested
    class CreateTask {
        @Test
        void shouldCreateTask() {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(TaskRequest.builder().name("Example").build())
                    .post("/api/v1/projects/{projectId}/tasks", project.getId());

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void shouldReturnBadRequestForInvalidTask() {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(TaskRequest.builder().build())
                    .post("/api/v1/projects/{projectId}/tasks", project.getId());

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetTasks {
        @Test
        void shouldReturnTasks() {
            taskRepository.save(
                    Task.builder().name("Example")
                            .dueDate(LocalDate.now().plusDays(1))
                            .project(project).build()
            );

            Response response = given()
//                    .param("name", "Johan")
                    .when()
                    .get("/api/v1/projects/{projectId}/tasks", project.getId());

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", equalTo(1));
        }
    }
}