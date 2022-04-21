package org.robbins.moviefinder.dtos;

public class ActorAlertDto {
    private Long actorId;

    public ActorAlertDto() {
    }

    public ActorAlertDto(Long actorId) {
        this.actorId = actorId;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }
}
