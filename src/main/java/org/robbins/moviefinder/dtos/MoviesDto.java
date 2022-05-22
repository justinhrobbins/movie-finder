package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoviesDto {

    private List<MovieDto> movies = Collections.synchronizedList(new ArrayList<>());
    private MovieCountsDto movieCounts;

    public List<MovieDto> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDto> movies) {
        this.movies = movies;
    }

    public MovieCountsDto getMovieCounts() {
        return movieCounts;
    }

    public void setMovieCounts(MovieCountsDto movieCounts) {
        this.movieCounts = movieCounts;
    }
}
