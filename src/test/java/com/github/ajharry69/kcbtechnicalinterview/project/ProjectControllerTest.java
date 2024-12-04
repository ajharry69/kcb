package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.models.Project;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProjectControllerTest {
    @Autowired
    private ProjectRepository repository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = RestAssured.DEFAULT_PORT;

        repository.deleteAll();
    }

    @Nested
    class CreateProject {
        @Test
        void shouldCreateProject() {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(ProjectRequest.builder().name("Example").build())
                    .post("/api/v1/projects");

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @Test
        void shouldReturnBadRequestForInvalidProject() {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(ProjectRequest.builder().build())
                    .post("/api/v1/projects");

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetProjects {
        @Test
        void shouldReturnProjects() {
            repository.save(Project.builder().name("Example").build());

            Response response = given()
                    .when()
                    .get("/api/v1/projects");

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", equalTo(1));
        }
    }
}