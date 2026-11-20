package com.example.githubsearcher.dto;

import java.util.List;

public class GitHubApiResponse {

    private List<GitHubRepositoryDto> items;

    public GitHubApiResponse() {
    }

    public List<GitHubRepositoryDto> getItems() {
        return items;
    }

    public void setItems(List<GitHubRepositoryDto> items) {
        this.items = items;
    }
}

