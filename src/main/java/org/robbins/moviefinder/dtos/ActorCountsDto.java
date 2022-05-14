package org.robbins.moviefinder.dtos;

public class ActorCountsDto {
    private long actorCount;
    private long upcomingMovieCount;
    private long recentMovieCount;
    private long subscriptionCount;

    public long getActorCount() {
        return actorCount;
    }

    public void setActorCount(long actorCount) {
        this.actorCount = actorCount;
    }

    public long getUpcomingMovieCount() {
        return upcomingMovieCount;
    }

    public void setUpcomingMovieCount(long upcomingMovieCount) {
        this.upcomingMovieCount = upcomingMovieCount;
    }

    public long getRecentMovieCount() {
        return recentMovieCount;
    }

    public void setRecentMovieCount(long recentMovieCount) {
        this.recentMovieCount = recentMovieCount;
    }

    public long getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(long subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }
}
