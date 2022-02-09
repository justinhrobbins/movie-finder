package org.robbins.moviefinder.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;

@RestController
@RequestMapping("movie")
public class MovieController {
    final TmdbApi tmdbApi;

    public MovieController (final TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @GetMapping("/{movieId}/watchproviders")
    public MovieDb geMovieWithWatchProviders(@PathVariable("movieId") final int movieId) {
        
        final MovieDb movieDb = tmdbApi.getMovies().getMovie(movieId, "en", MovieMethod.watch_providers);
        return movieDb;
    }

}
