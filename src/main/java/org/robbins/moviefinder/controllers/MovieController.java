package org.robbins.moviefinder.controllers;

import org.robbins.moviefinder.services.MovieService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;

@RestController
@CrossOrigin
@RequestMapping("movie")
public class MovieController {
    final TmdbApi tmdbApi;
    final MovieService movieService;

    public MovieController (final TmdbApi tmdbApi, final MovieService movieService) {
        this.tmdbApi = tmdbApi;
        this.movieService = movieService;
    }

    @GetMapping("/{movieId}/watchproviders")
    public MovieDb geMovieWithWatchProviders(@PathVariable("movieId") final int movieId) {
        return movieService.findMovieWatchProviders(movieId, "en");
    }

}
