
package com.example.githubsearcher.service;

import java.util.List;

import com.example.githubsearcher.dto.RepositoryResponse;
import com.example.githubsearcher.dto.SearchRequest;

public interface GitHubService {

    List<RepositoryResponse> searchRepositories(
            SearchRequest request
    );

    List<RepositoryResponse> getStoredRepositories(
            String language,
            Integer minStars,
            String sort
    );
}
