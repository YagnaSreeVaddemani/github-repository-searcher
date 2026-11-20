package com.example.githubsearcher.enums;

public enum SortField {

    STARS,
    FORKS,
    UPDATED;

    public static SortField from(String value) {

        if (value == null || value.isBlank()) {
            return STARS;
        }

        try {
            return SortField.valueOf(
                value.trim().toUpperCase()
            );
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                "Invalid sort value. Use stars, forks, or updated."
            );
        }
    }
}

