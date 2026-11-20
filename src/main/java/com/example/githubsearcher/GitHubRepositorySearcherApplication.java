

package com.example.githubsearcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitHubRepositorySearcherApplication {

    public static void main(String[] args) {

        SpringApplication.run(
            GitHubRepositorySearcherApplication.class, args
        );

        System.out.println("GitHub Repository Searcher Started!");
    }
}
