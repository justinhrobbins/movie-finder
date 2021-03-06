package org.robbins.moviefinder.dtos;

import info.movito.themoviedbapi.model.people.PersonCredits;
import info.movito.themoviedbapi.model.people.PersonPeople;

public class ActorDto {
    private Long actorId;
    private PersonPeople person;
    private MovieCountsDto movieCounts;
    private PersonCredits movieCredits;

    public ActorDto() {
    }

    public ActorDto(final Long actorId) {
        this.actorId = actorId;
    }

    public ActorDto(final Long actorId, final PersonPeople person) {
        this.actorId = actorId;
        this.person = person;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public PersonPeople getPerson() {
        return person;
    }

    public void setPerson(PersonPeople person) {
        this.person = person;
    }

    public MovieCountsDto getMovieCounts() {
        return movieCounts;
    }

    public void setMovieCounts(MovieCountsDto movieCounts) {
        this.movieCounts = movieCounts;
    }

    public PersonCredits getMovieCredits() {
        return movieCredits;
    }

    public void setMovieCredits(PersonCredits movieCredits) {
        this.movieCredits = movieCredits;
    }
    
}
