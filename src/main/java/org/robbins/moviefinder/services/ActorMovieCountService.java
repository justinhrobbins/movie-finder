package org.robbins.moviefinder.services;

import org.robbins.moviefinder.dtos.ActorMovieCountsDto;

public interface ActorMovieCountService {
    public ActorMovieCountsDto findActorMovieCounts(final Long actorId);
}
