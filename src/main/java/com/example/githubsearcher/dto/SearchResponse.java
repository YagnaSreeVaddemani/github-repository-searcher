
package com.example.githubsearcher.dto;

import java.util.List;

public class SearchResponse {

    private String message;

    private List<RepositoryResponse> repositories;

    public SearchResponse() {
    }

    public SearchResponse(
            String message,
            List<RepositoryResponse> repositories) {

        this.message = message;
        this.repositories = repositories;
    }

    public String getMessage() {
        return message;
    }

    public List<RepositoryResponse> getRepositories() {
        return repositories;
    }
}
