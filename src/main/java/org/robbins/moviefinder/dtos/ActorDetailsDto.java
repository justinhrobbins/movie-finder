package org.robbins.moviefinder.dtos;

import java.util.List;

public class ActorDetailsDto {
    long upcomingMovies;
    long recentMovies;
    long totalMovies;
    List<ActorMovieSubscriptionCounts> subscriptions;
 
    public ActorDetailsDto() {
    }

    public ActorDetailsDto(long totalMovies, long upcomingMovies, long recentMovies, List<ActorMovieSubscriptionCounts> subscriptions) {
        this.totalMovies = totalMovies;
        this.upcomingMovies = upcomingMovies;
        this.recentMovies = recentMovies;
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

    public List<ActorMovieSubscriptionCounts> getSubscriptions() {
        return subscriptions;
    }
}
