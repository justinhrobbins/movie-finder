package org.robbins.moviefinder.services.impl;

import org.robbins.moviefinder.services.MovieService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.ResponseStatusException;

@Service
public class MovieServiceImpl implements MovieService {
    final TmdbApi tmdbApi;

    public MovieServiceImpl(final TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @Override
    @Cacheable("moviewatchproviders")
    public MovieDb findMovieWatchProviders(int movieId, String language) {
        try {
            return tmdbApi.getMovies().getMovie(movieId, language, MovieMethod.watch_providers);
        } catch (ResponseStatusException ex) {
            return new MovieDb();   
        }
    }
    
}
