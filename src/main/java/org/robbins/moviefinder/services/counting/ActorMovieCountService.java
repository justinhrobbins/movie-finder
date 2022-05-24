package org.robbins.moviefinder.services.counting;

import java.util.Optional;

import org.robbins.moviefinder.dtos.MovieCountsDto;
import org.robbins.moviefinder.entities.User;

public interface ActorMovieCountService {
    public MovieCountsDto findActorMovieCounts(final Long actorId, final Optional<User> user);
}
