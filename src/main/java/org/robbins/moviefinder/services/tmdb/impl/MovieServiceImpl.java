package org.robbins.moviefinder.services.tmdb.impl;

import java.util.ArrayList;
import java.util.List;

import org.robbins.moviefinder.services.tmdb.MovieService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.ResponseStatusException;
import info.movito.themoviedbapi.model.people.PersonCast;

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

    @Override
    @Cacheable("moviecast")
    public List<PersonCast> findMovieCast(int movieId) {
        try {
            return tmdbApi.getMovies().getCredits(movieId).getCast();
        } catch (ResponseStatusException ex) {
            return new ArrayList<>();
        }
    }
}
