package org.robbins.moviefinder.controllers;

import java.util.List;

import org.robbins.moviefinder.services.tmdb.MovieService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

@RestController
@CrossOrigin
@RequestMapping("movies")
public class MovieController {
    final MovieService movieService;

    public MovieController (final MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{movieId}/watchproviders")
    public MovieDb geMovieWithWatchProviders(@PathVariable("movieId") final int movieId) {
        return movieService.findMovieWatchProviders(movieId, "en");
    }

    @GetMapping("/{movieId}/cast")
    public List<PersonCast> geMoviecast(@PathVariable("movieId") final int movieId) {
        return movieService.findMovieCast(movieId);
    }

}
