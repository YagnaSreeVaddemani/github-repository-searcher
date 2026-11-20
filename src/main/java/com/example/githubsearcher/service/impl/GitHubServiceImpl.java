
package com.example.githubsearcher.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.githubsearcher.client.GitHubApiClient;
import com.example.githubsearcher.dto.GitHubApiResponse;
import com.example.githubsearcher.dto.GitHubRepositoryDto;
import com.example.githubsearcher.dto.RepositoryResponse;
import com.example.githubsearcher.dto.SearchRequest;
import com.example.githubsearcher.entity.GitHubRepository;
import com.example.githubsearcher.enums.SortField;
import com.example.githubsearcher.repository.GitHubRepositoryDao;
import com.example.githubsearcher.service.GitHubService;

@Service
public class GitHubServiceImpl implements GitHubService {

    private final GitHubApiClient gitHubApiClient;
    private final GitHubRepositoryDao repositoryDao;

    public GitHubServiceImpl(
            GitHubApiClient gitHubApiClient,
            GitHubRepositoryDao repositoryDao) {

        this.gitHubApiClient = gitHubApiClient;
        this.repositoryDao = repositoryDao;
    }

    @Override
    @Transactional
    public List<RepositoryResponse> searchRepositories(
            SearchRequest request) {

        if (request == null || request.getQuery() == null
                || request.getQuery().isBlank()) {
            throw new IllegalArgumentException(
                    "Search query cannot be empty");
        }

        SortField sortField = SortField.from(request.getSort());

        GitHubApiResponse apiResponse =
                gitHubApiClient.searchRepositories(
                        request.getQuery(),
                        request.getLanguage(),
                        sortField.name().toLowerCase()
                );

        List<GitHubRepositoryDto> items =
                apiResponse.getItems();

        if (items == null || items.isEmpty()) {
            return List.of();
        }

        for (GitHubRepositoryDto dto : items) {

            if (dto.getId() == null) {
                continue;
            }

            GitHubRepository repository =
                    repositoryDao.findByGithubId(dto.getId())
                    .orElseGet(GitHubRepository::new);

            repository.setGithubId(dto.getId());
            repository.setName(dto.getName());
            repository.setDescription(dto.getDescription());

            if (dto.getOwner() != null) {
                repository.setOwner(dto.getOwner().getLogin());
            }

            repository.setLanguage(dto.getLanguage());
            repository.setStars(dto.getStars());
            repository.setForks(dto.getForks());
            repository.setLastUpdated(dto.getLastUpdated());

            repositoryDao.save(repository);
        }

        return items.stream()
                .filter(dto -> dto.getId() != null)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepositoryResponse> getStoredRepositories(
            String language,
            Integer minStars,
            String sort) {

        if (minStars != null && minStars < 0) {
            throw new IllegalArgumentException(
                    "Minimum stars cannot be negative");
        }

        SortField sortField = SortField.from(sort);

        String normalizedLanguage =
                language == null || language.isBlank()
                        ? null
                        : language.trim();

        Sort sorting;

        switch (sortField) {

            case STARS:
                sorting = Sort.by(
                        Sort.Direction.DESC, "stars");
                break;

            case FORKS:
                sorting = Sort.by(
                        Sort.Direction.DESC, "forks");
                break;

            case UPDATED:
                sorting = Sort.by(
                        Sort.Direction.DESC, "lastUpdated");
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid sort field");
        }

        List<GitHubRepository> repositories =
                repositoryDao.findRepositoriesByFilters(
                        normalizedLanguage,
                        minStars,
                        sorting
                );

        return repositories.stream()
                .map(this::mapEntityToResponse)
                .toList();
    }

    private RepositoryResponse mapToResponse(
            GitHubRepositoryDto dto) {

        String owner = dto.getOwner() == null
                ? null
                : dto.getOwner().getLogin();

        return new RepositoryResponse(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                owner,
                dto.getLanguage(),
                dto.getStars(),
                dto.getForks(),
                dto.getLastUpdated()
        );
    }

    private RepositoryResponse mapEntityToResponse(
            GitHubRepository entity) {

        return new RepositoryResponse(
                entity.getGithubId(),
                entity.getName(),
                entity.getDescription(),
                entity.getOwner(),
                entity.getLanguage(),
                entity.getStars(),
                entity.getForks(),
                entity.getLastUpdated()
        );
    }
}
