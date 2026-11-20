
package com.example.githubsearcher.dto;

import java.time.OffsetDateTime;

public class RepositoryResponse {

    private Long id;
    private String name;
    private String description;
    private String owner;
    private String language;
    private int stars;
    private int forks;
    private OffsetDateTime lastUpdated;

    public RepositoryResponse() {
    }

    public RepositoryResponse(
            Long id,
            String name,
            String description,
            String owner,
            String language,
            int stars,
            int forks,
            OffsetDateTime lastUpdated) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.language = language;
        this.stars = stars;
        this.forks = forks;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public String getLanguage() {
        return language;
    }

    public int getStars() {
        return stars;
    }

    public int getForks() {
        return forks;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }
}
