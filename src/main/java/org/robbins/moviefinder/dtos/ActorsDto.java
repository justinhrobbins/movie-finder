package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.List;

public class ActorsDto {

    private List<ActorDto> actors = new ArrayList<>();
    private int actorCount;
    private int upcomingMovieCount;
    private int recentMovieCount;
    private int subscriptionCount;
    
    public ActorsDto() {
    }

    public List<ActorDto> getActors() {
        return actors;
    }

    public void setActors(List<ActorDto> actors) {
        this.actors = actors;
    }

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
