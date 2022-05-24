package org.robbins.moviefinder.services;

import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.enums.ActorSort;
import org.robbins.moviefinder.enums.MovieFilter;

public interface MyActorService {
    public ActorsDto findAMyActors(final String userEmail, final Optional<MovieFilter> filter,
            final Optional<ActorSort> sort);
}
