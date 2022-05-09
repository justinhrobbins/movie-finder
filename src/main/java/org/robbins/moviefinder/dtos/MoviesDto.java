package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.List;

public class MoviesDto {

    private List<ActorDto> actors = new ArrayList<>();

    public List<ActorDto> getActors() {
        return actors;
    }

    public void setActors(List<ActorDto> actors) {
        this.actors = actors;
    }
    
}
