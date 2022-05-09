package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.List;

public class ActorsDto {

    private List<ActorDto> actors = new ArrayList<>();
    private ActorCountsDto counts;
    
    public ActorsDto() {
    }

    public List<ActorDto> getActors() {
        return actors;
    }

    public void setActors(List<ActorDto> actors) {
        this.actors = actors;
    }

    public ActorCountsDto getCounts() {
        return counts;
    }

    public void setCounts(ActorCountsDto counts) {
        this.counts = counts;
    }
}
