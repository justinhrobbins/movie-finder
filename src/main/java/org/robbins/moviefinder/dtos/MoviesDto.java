package org.robbins.moviefinder.dtos;

public class MoviesDto {

    private ActorsDto actors = new ActorsDto();
    private MovieCountsDto movieCounts;

    public ActorsDto getActors() {
        return actors;
    }

    public void setActors(ActorsDto actors) {
        this.actors = actors;
    }

    public MovieCountsDto getMovieCounts() {
        return movieCounts;
    }

    public void setMovieCounts(MovieCountsDto movieCounts) {
        this.movieCounts = movieCounts;
    }

}
