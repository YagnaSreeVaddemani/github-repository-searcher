
package com.example.githubsearcher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SearchRequest {

    @NotBlank(message = "Search query cannot be empty")
    @Size(max = 200, message = "Search query is too long")
    private String query;

    private String language;

    private String sort;

    public SearchRequest() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
