package org.robbins.moviefinder.dtos;

import java.util.List;

public class ActorDetailsDto {
    long upcomingMovies;
    long recentMovies;
    long totalMovies;
    List<ActorMovieSubscriptionCountsDto> subscriptions;
 
    public ActorDetailsDto() {
    }

    public ActorDetailsDto(long totalMovies, long upcomingMovies, long recentMovies, List<ActorMovieSubscriptionCountsDto> subscriptions) {
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

    public List<ActorMovieSubscriptionCountsDto> getSubscriptions() {
        return subscriptions;
    }
}
