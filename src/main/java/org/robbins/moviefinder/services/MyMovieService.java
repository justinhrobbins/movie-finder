package org.robbins.moviefinder.services;

import java.util.Optional;

import org.robbins.moviefinder.dtos.MoviesDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.MovieFilter;

public interface MyMovieService {
    public MoviesDto findMyMovies(final User user, final Optional<MovieFilter> filter);
}
