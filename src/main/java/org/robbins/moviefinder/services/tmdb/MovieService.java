package org.robbins.moviefinder.services.tmdb;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorCastDto;
import org.robbins.moviefinder.entities.User;

import info.movito.themoviedbapi.model.MovieDb;

public interface MovieService {
    public MovieDb findMovieWatchProviders(final int movieId, final String language);
    public List<ActorCastDto> findMovieCast(final int movieId, final Optional<User> user);
}
