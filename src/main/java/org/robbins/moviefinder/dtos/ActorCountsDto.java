package org.robbins.moviefinder.dtos;

public class ActorCountsDto {
    private int actorCount;
    private int upcomingMovieCount;
    private int recentMovieCount;
    private int subscriptionCount;

    public int getActorCount() {
        return actorCount;
    }

    public void setActorCount(int actorCount) {
        this.actorCount = actorCount;
    }

    public int getUpcomingMovieCount() {
        return upcomingMovieCount;
    }

    public void setUpcomingMovieCount(int upcomingMovieCount) {
        this.upcomingMovieCount = upcomingMovieCount;
    }

    public int getRecentMovieCount() {
        return recentMovieCount;
    }

    public void setRecentMovieCount(int recentMovieCount) {
        this.recentMovieCount = recentMovieCount;
    }

    public int getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(int subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }
}
