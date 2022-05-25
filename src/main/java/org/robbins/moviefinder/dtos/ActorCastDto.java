package org.robbins.moviefinder.dtos;

import info.movito.themoviedbapi.model.people.PersonCast;

public class ActorCastDto {
    private Long actorId;
    private PersonCast person;
    private boolean isFollowedByUser;

    public ActorCastDto() {
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public PersonCast getPerson() {
        return person;
    }

    public void setPerson(PersonCast person) {
        this.person = person;
    }

    public boolean isFollowedByUser() {
        return isFollowedByUser;
    }

    public void setFollowedByUser(boolean isFollowedByUser) {
        this.isFollowedByUser = isFollowedByUser;
    }
}
