

package com.example.githubsearcher.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.githubsearcher.entity.GitHubRepository;

public interface GitHubRepositoryDao
        extends JpaRepository<GitHubRepository, Long> {

    Optional<GitHubRepository> findByGithubId(Long githubId);

    @Query("""
        SELECT r FROM GitHubRepository r
        WHERE (
            :language IS NULL
            OR r.language = :language
        )
        AND (
            :minStars IS NULL
            OR r.stars >= :minStars
        )
        """)
    List<GitHubRepository> findRepositoriesByFilters(
            @Param("language") String language,
            @Param("minStars") Integer minStars,
            Sort sort
    );
}
