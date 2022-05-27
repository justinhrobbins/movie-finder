package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import info.movito.themoviedbapi.model.people.PersonCredit;

public class MovieDto {
    PersonCredit credit;
    List<ActorDto> actors = new ArrayList<>();

    public MovieDto() {
    }

    public PersonCredit getCredit() {
        return credit;
    }

    public void setCredit(PersonCredit credit) {
        this.credit = credit;
    }

    public List<ActorDto> getActors() {
        return actors;
    }

    public void setActors(List<ActorDto> actors) {
        this.actors = actors;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCredit().getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MovieDto))
            return false;
        MovieDto movieDto = (MovieDto) o;
        return Objects.equals(getCredit().getId(), movieDto.getCredit().getId());
    }

}