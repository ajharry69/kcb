package com.github.ajharry69.kcbtechnicalinterview.project;

import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectRequest;
import com.github.ajharry69.kcbtechnicalinterview.project.models.ProjectResponse;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.util.UriComponents;

import java.util.List;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebAppConfiguration
class ProjectControllerTest {
    @Nested
    class CreateProject {
        @Test
        void shouldCreateProject() {
            ProjectService service = mock(ProjectService.class);
            HateoasPageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = mock(HateoasPageableHandlerMethodArgumentResolver.class);
            UriComponents uriComponents = mock(UriComponents.class);
            PagedResourcesAssembler<ProjectResponse> pagedResourcesAssembler = new PagedResourcesAssembler<>(
                    pageableHandlerMethodArgumentResolver,
                    uriComponents
            );

            MockMvcResponse response = given()
                    .standaloneSetup(new ProjectController(service, pagedResourcesAssembler))
                    .body(ProjectRequest.builder().name("Example").build())
                    .when()
                    .post("/projects");

            response.prettyPrint();

            response
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void shouldReturnBadRequestForInvalidProject() {
            ProjectService service = mock(ProjectService.class);
            HateoasPageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = mock(HateoasPageableHandlerMethodArgumentResolver.class);
            UriComponents uriComponents = mock(UriComponents.class);
            PagedResourcesAssembler<ProjectResponse> pagedResourcesAssembler = new PagedResourcesAssembler<>(
                    pageableHandlerMethodArgumentResolver,
                    uriComponents
            );

            MockMvcResponse response = given()
                    .standaloneSetup(new ProjectController(service, pagedResourcesAssembler))
                    .body(ProjectRequest.builder().build())
                    .when()
                    .post("/projects");

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
            when(service.getProjects(any()))
                    .thenReturn(new PageImpl<>(List.of(ProjectResponse.builder().id(UUID.randomUUID()).name("Example").build())));
            HateoasPageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = mock(HateoasPageableHandlerMethodArgumentResolver.class);
            UriComponents uriComponents = mock(UriComponents.class);
            PagedResourcesAssembler<ProjectResponse> pagedResourcesAssembler = new PagedResourcesAssembler<>(
                    pageableHandlerMethodArgumentResolver,
                    uriComponents
            );

            given()
                    .standaloneSetup(new ProjectController(service, pagedResourcesAssembler))
                    .when()
                    .get("/projects")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", equalTo(1));
        }
    }
}