package org.robbins.moviefinder.services;

import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.entities.User;

public interface ActorsCountService {
    public ActorCountsDto calculateTotals(final ActorsDto actors, final User user);
}
