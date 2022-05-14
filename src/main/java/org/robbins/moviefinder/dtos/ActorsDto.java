package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActorsDto {

    private List<ActorDto> actors = Collections.synchronizedList(new ArrayList<>());
    private ActorCountsDto actorCounts;
    private MovieCountsDto movieCounts;
    
    public ActorsDto() {
    }

    public List<ActorDto> getActors() {
        return actors;
    }

    public void setActors(List<ActorDto> actors) {
        this.actors = actors;
    }

    public ActorCountsDto getActorCounts() {
        return actorCounts;
    }

    public void setActorCounts(ActorCountsDto actorCounts) {
        this.actorCounts = actorCounts;
    }

    public MovieCountsDto getMovieCounts() {
        return movieCounts;
    }

    public void setMovieCounts(MovieCountsDto movieCounts) {
        this.movieCounts = movieCounts;
    }

    
}
