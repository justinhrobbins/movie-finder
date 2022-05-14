package org.robbins.moviefinder.dtos;

public class ActorAlertDto {
    private final Long actorId;

    public ActorAlertDto(Long actorId) {
        this.actorId = actorId;
    }

    public Long getActorId() {
        return actorId;
    }
}
