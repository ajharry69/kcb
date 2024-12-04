package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskRequest;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskResponse;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.util.UriComponents;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebAppConfiguration
class TaskControllerTest {
    @Nested
    class CreateTask {
        @Test
        void shouldCreateTask() {
            TaskService service = mock(TaskService.class);
            HateoasPageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = mock(HateoasPageableHandlerMethodArgumentResolver.class);
            UriComponents uriComponents = mock(UriComponents.class);
            PagedResourcesAssembler<TaskResponse> pagedResourcesAssembler = new PagedResourcesAssembler<>(
                    pageableHandlerMethodArgumentResolver,
                    uriComponents
            );

            MockMvcResponse response = given()
                    .standaloneSetup(new TaskController(service, pagedResourcesAssembler))
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
            HateoasPageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = mock(HateoasPageableHandlerMethodArgumentResolver.class);
            UriComponents uriComponents = mock(UriComponents.class);
            PagedResourcesAssembler<TaskResponse> pagedResourcesAssembler = new PagedResourcesAssembler<>(
                    pageableHandlerMethodArgumentResolver,
                    uriComponents
            );

            MockMvcResponse response = given()
                    .standaloneSetup(new TaskController(service, pagedResourcesAssembler))
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
            var projectId = UUID.randomUUID();
            TaskService service = mock(TaskService.class);
            when(service.getTasks(projectId, TaskStatus.TO_DO, LocalDate.now(), Pageable.ofSize(20)))
                    .thenReturn(new PageImpl<>(List.of(TaskResponse.builder().id(UUID.randomUUID()).name("Example").build())));
            HateoasPageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = mock(HateoasPageableHandlerMethodArgumentResolver.class);
            UriComponents uriComponents = mock(UriComponents.class);
            PagedResourcesAssembler<TaskResponse> pagedResourcesAssembler = new PagedResourcesAssembler<>(
                    pageableHandlerMethodArgumentResolver,
                    uriComponents
            );

            given()
                    .standaloneSetup(new TaskController(service, pagedResourcesAssembler))
//                    .param("name", "Johan")
                    .when()
                    .get("/projects/{projectId}/tasks", projectId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", equalTo(1));
        }
    }
}