package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.List;

public class ActorAlertsDto {
    private List<ActorAlertDto> actorAlerts = new ArrayList<>();
    
    public ActorAlertsDto() {
    }

    public List<ActorAlertDto> getActorAlerts() {
        return actorAlerts;
    }

    public void setActorAlerts(List<ActorAlertDto> actorAlerts) {
        this.actorAlerts = actorAlerts;
    }

    
}
