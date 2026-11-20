
package com.example.githubsearcher.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.example.githubsearcher.client.GitHubApiClient;
import com.example.githubsearcher.dto.GitHubApiResponse;
import com.example.githubsearcher.dto.GitHubRepositoryDto;
import com.example.githubsearcher.dto.RepositoryResponse;
import com.example.githubsearcher.dto.SearchRequest;
import com.example.githubsearcher.entity.GitHubRepository;
import com.example.githubsearcher.repository.GitHubRepositoryDao;
import com.example.githubsearcher.service.impl.GitHubServiceImpl;

@ExtendWith(MockitoExtension.class)
class GitHubServiceImplTest {

    @Mock
    private GitHubApiClient gitHubApiClient;

    @Mock
    private GitHubRepositoryDao repositoryDao;

    @InjectMocks
    private GitHubServiceImpl gitHubService;

    @Test
    void searchRepositories_shouldSaveAndReturnRepositories() {

        SearchRequest request = new SearchRequest();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        GitHubRepositoryDto dto = new GitHubRepositoryDto();
        dto.setId(101L);
        dto.setName("spring-boot");
        dto.setDescription("Spring Boot Framework");
        dto.setLanguage("Java");
        dto.setStars(100);
        dto.setForks(20);

        GitHubRepositoryDto.Owner owner =
                new GitHubRepositoryDto.Owner();
        owner.setLogin("spring-projects");
        dto.setOwner(owner);

        GitHubApiResponse apiResponse = new GitHubApiResponse();
        apiResponse.setItems(List.of(dto));

        when(gitHubApiClient.searchRepositories(
                "spring boot", "Java", "stars"))
                .thenReturn(apiResponse);

        when(repositoryDao.findByGithubId(101L))
                .thenReturn(Optional.empty());

        when(repositoryDao.save(any(GitHubRepository.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<RepositoryResponse> result =
                gitHubService.searchRepositories(request);

        assertEquals(1, result.size());
        assertEquals("spring-boot", result.get(0).getName());
        assertEquals("Java", result.get(0).getLanguage());
        assertEquals(100, result.get(0).getStars());
        assertEquals("spring-projects", result.get(0).getOwner());

        verify(repositoryDao, times(1))
                .save(any(GitHubRepository.class));
    }

    @Test
    void searchRepositories_shouldRejectEmptyQuery() {

        SearchRequest request = new SearchRequest();
        request.setQuery(" ");

        assertThrows(IllegalArgumentException.class,
                () -> gitHubService.searchRepositories(request));

        verifyNoInteractions(gitHubApiClient);
    }

    @Test
    void searchRepositories_shouldReturnEmptyListWhenNoResults() {

        SearchRequest request = new SearchRequest();
        request.setQuery("unknown");

        GitHubApiResponse apiResponse = new GitHubApiResponse();
        apiResponse.setItems(List.of());

        when(gitHubApiClient.searchRepositories(
                eq("unknown"), isNull(), anyString()))
                .thenReturn(apiResponse);

        List<RepositoryResponse> result =
                gitHubService.searchRepositories(request);

        assertTrue(result.isEmpty());

        verify(repositoryDao, never())
                .save(any(GitHubRepository.class));
    }

    @Test
    void getStoredRepositories_shouldReturnRepositories() {

        GitHubRepository entity = new GitHubRepository();
        entity.setGithubId(101L);
        entity.setName("spring-boot");
        entity.setLanguage("Java");
        entity.setStars(500);

        when(repositoryDao.findRepositoriesByFilters(
                isNull(), eq(100), any(Sort.class)))
                .thenReturn(List.of(entity));

        List<RepositoryResponse> result =
                gitHubService.getStoredRepositories(
                        null, 100, "stars");

        assertEquals(1, result.size());
        assertEquals("spring-boot", result.get(0).getName());
        assertEquals(500, result.get(0).getStars());
    }

    @Test
    void getStoredRepositories_shouldRejectNegativeMinimumStars() {

        assertThrows(IllegalArgumentException.class,
                () -> gitHubService.getStoredRepositories(
                        null, -1, "stars"));

        verifyNoInteractions(repositoryDao);
    }

    @Test
    void getStoredRepositories_shouldTrimLanguage() {

        when(repositoryDao.findRepositoriesByFilters(
                eq("Java"), isNull(), any(Sort.class)))
                .thenReturn(List.of());

        List<RepositoryResponse> result =
                gitHubService.getStoredRepositories(
                        " Java ", null, "stars");

        assertTrue(result.isEmpty());

        verify(repositoryDao).findRepositoriesByFilters(
                eq("Java"), isNull(), any(Sort.class));
    }
}
