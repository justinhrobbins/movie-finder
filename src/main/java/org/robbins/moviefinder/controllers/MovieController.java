package org.robbins.moviefinder.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorCastDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.UserService;
import org.robbins.moviefinder.services.tmdb.MovieService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import info.movito.themoviedbapi.model.MovieDb;

@RestController
@CrossOrigin
@RequestMapping("movies")
public class MovieController extends AbstractController{
    private final MovieService movieService;
    private final UserService userService;

    public MovieController (final MovieService movieService, final UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @GetMapping("/{movieId}/watchproviders")
    public MovieDb geMovieWithWatchProviders(@PathVariable("movieId") final int movieId) {
        return movieService.findMovieWatchProviders(movieId, "en");
    }

    @GetMapping("/{movieId}/cast")
    public List<ActorCastDto> geMoviecast(@PathVariable("movieId") final int movieId, final Principal principal) {
        final Optional<User> user = extractUserFromPrincipal(principal);
        return movieService.findMovieCast(movieId, user);
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
