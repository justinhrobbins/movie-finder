package org.robbins.moviefinder.services.tmdb;

import info.movito.themoviedbapi.model.MovieDb;

public interface MovieService {
    public MovieDb findMovieWatchProviders(final int movieId, final String language);
}
