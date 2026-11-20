package com.example.githubsearcher.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.githubsearcher.dto.RepositoryResponse;
import com.example.githubsearcher.dto.RepositoriesResponse;
import com.example.githubsearcher.dto.SearchRequest;
import com.example.githubsearcher.dto.SearchResponse;
import com.example.githubsearcher.service.GitHubService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    // POST: Search GitHub repositories

    @PostMapping("/search")
    public ResponseEntity<SearchResponse> searchRepositories(
            @Valid @RequestBody SearchRequest request) {

        List<RepositoryResponse> repositories =
                gitHubService.searchRepositories(request);

        SearchResponse response = new SearchResponse(
                "Repositories fetched and saved successfully",
                repositories
        );

        return ResponseEntity.ok(response);
    }

    // GET: Retrieve stored repositories

    @GetMapping("/repositories")
    public ResponseEntity<RepositoriesResponse> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort) {

        List<RepositoryResponse> repositories =
                gitHubService.getStoredRepositories(
                        language,
                        minStars,
                        sort
                );

        return ResponseEntity.ok(
                new RepositoriesResponse(repositories)
        );
    }
}
