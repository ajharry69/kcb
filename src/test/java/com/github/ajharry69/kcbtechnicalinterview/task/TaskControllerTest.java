package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskRequest;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskResponse;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
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
class TaskControllerTest {
    /*@Before
    public void setup() {
        RestAssured.port = RestAssured.DEFAULT_PORT;
    }*/

    @Nested
    class CreateTask {
        @Test
        void shouldCreateTask() {
            TaskService service = mock(TaskService.class);

            MockMvcResponse response = given()
                    .standaloneSetup(new TaskController(service))
                    .body(TaskRequest.builder().name("Example").build())
                    .when()
                    .post("/projects/{projectId}/tasks", UUID.randomUUID());

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void shouldReturnBadRequestForInvalidTask() {
            TaskService service = mock(TaskService.class);

            MockMvcResponse response = given()
                    .standaloneSetup(new TaskController(service))
                    .body(TaskRequest.builder().build())
                    .when()
                    .post("/projects/{projectId}/tasks", UUID.randomUUID());

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
            TaskService service = mock(TaskService.class);
            when(service.getTasks(Mockito.any(), TaskStatus.TO_DO, LocalDate.now(), Pageable.ofSize(20)))
                    .thenReturn(List.of(TaskResponse.builder().id(UUID.randomUUID()).name("Example").build()));
            given()
                    .standaloneSetup(new TaskController(service))
//                    .param("name", "Johan")
                    .when()
                    .get("/projects/{projectId}/tasks", UUID.randomUUID())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", equalTo(1));
        }
    }
}