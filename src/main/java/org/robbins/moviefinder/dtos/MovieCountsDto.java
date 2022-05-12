package org.robbins.moviefinder.dtos;

import java.util.List;

public class MovieCountsDto {
    final long upcomingMovies;
    final long recentMovies;
    final long totalMovies;
    final long moviesOnSubscriptions;
    final List<ActorMovieSubscriptionCountsDto> subscriptions;

    public MovieCountsDto(final long totalMovies, final long upcomingMovies,
            final long recentMovies, final long moviesOnSubscriptions,
            final List<ActorMovieSubscriptionCountsDto> subscriptions) {
        this.totalMovies = totalMovies;
        this.upcomingMovies = upcomingMovies;
        this.recentMovies = recentMovies;
        this.moviesOnSubscriptions = moviesOnSubscriptions;
        this.subscriptions = subscriptions;
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

    public List<ActorMovieSubscriptionCountsDto> getSubscriptions() {
        return subscriptions;
    }
}
