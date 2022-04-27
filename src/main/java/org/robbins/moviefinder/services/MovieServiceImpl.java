package org.robbins.moviefinder.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;

@Service
public class MovieServiceImpl implements MovieService {
    final TmdbApi tmdbApi;

    public MovieServiceImpl(final TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @Override
    @Cacheable("moviewatchproviders")
    public MovieDb findMovieWatchProviders(int movieId, String language) {
       return tmdbApi.getMovies().getMovie(movieId, language, MovieMethod.watch_providers);
    }
    
}
