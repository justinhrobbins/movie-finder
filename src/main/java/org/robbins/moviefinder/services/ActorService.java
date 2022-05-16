package org.robbins.moviefinder.services;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.Filters;
import org.robbins.moviefinder.entities.User;

public interface ActorService {
    public ActorDto findActor(final Long actorId);

    public ActorsDto findActors(final List<Long> actorIds);

    public ActorsDto findPopularActors();

    public ActorDto findActorWithMovies(final Long actorId, final Optional<Filters> filter, final Optional<User> user);
}
