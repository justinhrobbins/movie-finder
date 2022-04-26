package org.robbins.moviefinder.dtos;

import info.movito.themoviedbapi.model.people.Person;

public class ActorAlertDto {
    private Long actorId;
    private Person person;
    private ActorDetailsDto details;

    public ActorAlertDto() {
    }

    public ActorAlertDto(final Long actorId) {
        this.actorId = actorId;
    }

    public ActorAlertDto(final Long actorId, final Person person) {
        this.actorId = actorId;
        this.person = person;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ActorDetailsDto getDetails() {
        return details;
    }

    public void setDetails(ActorDetailsDto details) {
        this.details = details;
    }

    
}
