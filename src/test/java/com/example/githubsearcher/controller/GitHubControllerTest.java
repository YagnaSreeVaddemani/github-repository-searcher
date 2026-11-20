
package com.example.githubsearcher.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.example.githubsearcher.dto.RepositoryResponse;
import com.example.githubsearcher.service.GitHubService;

@ExtendWith(MockitoExtension.class)
class GitHubControllerTest {

    @Mock
    private GitHubService gitHubService;

    private MockMvc mockMvc;

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        GitHubController controller =
                new GitHubController(gitHubService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(validator)
                .build();
    }

    @Test
    void searchRepositories_shouldReturn200() throws Exception {

        RepositoryResponse repository = new RepositoryResponse(
                101L,
                "spring-boot",
                "Spring Boot Framework",
                "spring-projects",
                "Java",
                500,
                100,
                null
        );

        when(gitHubService.searchRepositories(any()))
                .thenReturn(List.of(repository));

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "query": "spring boot",
                            "language": "Java",
                            "sort": "stars"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Repositories fetched and saved successfully"))
                .andExpect(jsonPath("$.repositories[0].name")
                        .value("spring-boot"))
                .andExpect(jsonPath("$.repositories[0].language")
                        .value("Java"))
                .andExpect(jsonPath("$.repositories[0].stars")
                        .value(500));

        verify(gitHubService, times(1))
                .searchRepositories(any());
    }

    @Test
    void searchRepositories_shouldReturn400ForEmptyQuery()
            throws Exception {

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "query": " ",
                            "language": "Java",
                            "sort": "stars"
                        }
                        """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(gitHubService);
    }

    @Test
    void getRepositories_shouldReturn200() throws Exception {

        RepositoryResponse repository = new RepositoryResponse(
                101L,
                "spring-boot",
                "Spring Boot Framework",
                "spring-projects",
                "Java",
                500,
                100,
                null
        );

        when(gitHubService.getStoredRepositories(
                null, null, "stars"))
                .thenReturn(List.of(repository));

        mockMvc.perform(get("/api/github/repositories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories[0].name")
                        .value("spring-boot"))
                .andExpect(jsonPath("$.repositories[0].stars")
                        .value(500));

        verify(gitHubService)
                .getStoredRepositories(null, null, "stars");
    }

    @Test
    void getRepositories_shouldApplyFiltersAndSorting()
            throws Exception {

        RepositoryResponse repository = new RepositoryResponse(
                102L,
                "spring-framework",
                "Spring Framework",
                "spring-projects",
                "Java",
                1000,
                200,
                null
        );

        when(gitHubService.getStoredRepositories(
                "Java", 100, "forks"))
                .thenReturn(List.of(repository));

        mockMvc.perform(get("/api/github/repositories")
                .param("language", "Java")
                .param("minStars", "100")
                .param("sort", "forks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories[0].name")
                        .value("spring-framework"))
                .andExpect(jsonPath("$.repositories[0].stars")
                        .value(1000));

        verify(gitHubService)
                .getStoredRepositories("Java", 100, "forks");
    }
}
