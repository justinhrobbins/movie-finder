package org.robbins.moviefinder.services;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorMovieCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;

public interface ActorService {
    public ActorMovieCountsDto findActorMovieCounts(final Long actorId);
    public ActorsDto findPopularActors();
    public ActorDto findByActorId(final Long actorId);
}
