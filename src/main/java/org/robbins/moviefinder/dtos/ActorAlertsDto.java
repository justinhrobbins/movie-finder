package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.List;

public class ActorAlertsDto {

    private List<ActorAlertDto> actorAlerts = new ArrayList<>();
    private int actorAlertCount;
    private int upcomingMovieCount;
    private int recentMovieCount;
    
    public ActorAlertsDto() {
    }

    public List<ActorAlertDto> getActorAlerts() {
        return actorAlerts;
    }

    public void setActorAlerts(List<ActorAlertDto> actorAlerts) {
        this.actorAlerts = actorAlerts;
    }

    public int getActorAlertCount() {
        return actorAlertCount;
    }

    public void setActorAlertCount(int actorAlertCount) {
        this.actorAlertCount = actorAlertCount;
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

    
}
