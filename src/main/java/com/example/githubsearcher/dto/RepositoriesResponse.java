package com.example.githubsearcher.dto;

import java.util.List;

public class RepositoriesResponse {

    private List<RepositoryResponse> repositories;

    public RepositoriesResponse() {
    }

    public RepositoriesResponse(List<RepositoryResponse> repositories) {
        this.repositories = repositories;
    }

    public List<RepositoryResponse> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<RepositoryResponse> repositories) {
        this.repositories = repositories;
    }
}
