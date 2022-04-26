package org.robbins.moviefinder.dtos;

public class ActorDetailsDto {
    long upcomingMovies;
    long recentMovies;
    long totalMovies;

    public ActorDetailsDto() {
    }

    public ActorDetailsDto(long totalMovies, long upcomingMovies, long recentMovies) {
        this.totalMovies = totalMovies;
        this.upcomingMovies = upcomingMovies;
        this.recentMovies = recentMovies;
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
}
