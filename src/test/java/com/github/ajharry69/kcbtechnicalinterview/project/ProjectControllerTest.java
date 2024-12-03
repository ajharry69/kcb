package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = MyConfiguration.class)
@WebAppConfiguration
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectControllerTest {
    /*@Before
    public void setup() {
        RestAssured.port = RestAssured.DEFAULT_PORT;
    }*/

    @Nested
    class CreateProject {
        @Test
        void shouldCreateProject() {
            ProjectService service = mock(ProjectService.class);

            MockMvcResponse response = given()
                    .standaloneSetup(new ProjectController(service))
                    .body(ProjectRequest.builder().name("Example").build())
                    .when()
                    .post("/api/v1/projects");

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void shouldReturnBadRequestForInvalidProject() {
            ProjectService service = mock(ProjectService.class);

            MockMvcResponse response = given()
                    .standaloneSetup(new ProjectController(service))
                    .body(ProjectRequest.builder().build())
                    .when()
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
            ProjectService service = mock(ProjectService.class);
            when(service.getProjects())
                    .thenReturn(List.of(ProjectResponse.builder().id(UUID.randomUUID()).name("Example").build()));
            given()
                    .standaloneSetup(new ProjectController(service))
//                    .param("name", "Johan")
                    .when()
                    .get("/api/v1/projects")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", equalTo(1));
        }
    }
}