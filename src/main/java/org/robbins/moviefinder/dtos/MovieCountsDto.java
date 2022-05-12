package org.robbins.moviefinder.dtos;

public class MovieCountsDto {
    final long upcomingMovies;
    final long recentMovies;
    final long totalMovies;
    final long moviesOnSubscriptions;

    public MovieCountsDto(final long totalMovies, final long upcomingMovies,
            final long recentMovies, final long moviesOnSubscriptions) {
        this.totalMovies = totalMovies;
        this.upcomingMovies = upcomingMovies;
        this.recentMovies = recentMovies;
        this.moviesOnSubscriptions = moviesOnSubscriptions;
    }

    public long getUpcomingMovies() {
        return upcomingMovies;
    }

    public long getRecentMovies() {
        return recentMovies;
    }

    public long getTotalMovies() {
        return totalMovies;
    }

    public long getMoviesOnSubscriptions() {
        return moviesOnSubscriptions;
    }
}
