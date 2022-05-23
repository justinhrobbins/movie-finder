package org.robbins.moviefinder.services;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.enums.MovieSort;

public interface ActorService {
    public ActorDto findActor(final Long actorId);

    public ActorsDto findActors(final List<Long> actorIds);

    public ActorsDto findPopularActors();

    public ActorDto findActorWithMovies(final Long actorId, final Optional<MovieFilter> filter,
            final Optional<MovieSort> sort, final Optional<User> user);
}
