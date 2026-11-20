
package com.example.githubsearcher.client;

import java.util.Locale;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;

import com.example.githubsearcher.dto.GitHubApiResponse;
import com.example.githubsearcher.exception.GitHubApiException;

@Component
public class GitHubApiClient {

    private final RestClient restClient;

    public GitHubApiClient(RestClient githubRestClient) {
        this.restClient = githubRestClient;
    }

    public GitHubApiResponse searchRepositories(
            String query,
            String language,
            String sort) {

        try {

            // Step 1: Validate search query
            if (query == null || query.isBlank()) {
                throw new IllegalArgumentException(
                        "Search query cannot be empty");
            }

            // Step 2: Validate sorting
            String sortField = sort == null || sort.isBlank()
                    ? "stars"
                    : sort.trim().toLowerCase(Locale.ROOT);

            if (!sortField.equals("stars")
                    && !sortField.equals("forks")
                    && !sortField.equals("updated")) {

                throw new IllegalArgumentException(
                        "Invalid sort field. Use stars, forks or updated");
            }

            // Step 3: Build GitHub search query
            StringBuilder queryBuilder =
                    new StringBuilder(query.trim());

            if (language != null && !language.isBlank()) {
                queryBuilder.append(" language:")
                        .append(language.trim());
            }

            final String searchQuery = queryBuilder.toString();

            // Step 4: Call GitHub REST API
            GitHubApiResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/repositories")
                            .queryParam("q", searchQuery)
                            .queryParam("sort", sortField)
                            .queryParam("page", 1)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            (request, apiResponse) -> {

                                int status =
                                        apiResponse.getStatusCode().value();

                                if (status == 403 || status == 429) {
                                    throw new GitHubApiException(
                                            "GitHub access denied or API rate limit exceeded. "
                                                    + "Please try again later.",
                                            status);
                                }

                                throw new GitHubApiException(
                                        "GitHub API returned HTTP " + status,
                                        status);
                            })
                    .body(GitHubApiResponse.class);

            // Step 5: Validate GitHub response
            if (response == null || response.getItems() == null) {
                throw new GitHubApiException(
                        "Invalid response received from GitHub",
                        502);
            }

            return response;

        } catch (GitHubApiException ex) {

            // Re-throw custom GitHub API exception
            throw ex;

        } catch (RestClientResponseException ex) {

            // Handle HTTP response errors
            int status = ex.getStatusCode().value();

            if (status == 403 || status == 429) {
                throw new GitHubApiException(
                        "GitHub API rate limit exceeded. Try again later.",
                        status,
                        ex);
            }

            throw new GitHubApiException(
                    "GitHub API request failed: " + ex.getStatusText(),
                    status,
                    ex);

        } catch (ResourceAccessException ex) {

            // Handle connection errors
            throw new GitHubApiException(
                    "Unable to connect to GitHub. Check your internet connection and try again.",
                    503,
                    ex);
        }
    }
}
