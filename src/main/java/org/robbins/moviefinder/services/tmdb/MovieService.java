package org.robbins.moviefinder.services.tmdb;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

public interface MovieService {
    public MovieDb findMovieWatchProviders(final int movieId, final String language);
    public List<PersonCast> findMovieCast(final int movieId);
}
