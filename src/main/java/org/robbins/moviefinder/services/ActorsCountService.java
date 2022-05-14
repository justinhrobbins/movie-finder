package org.robbins.moviefinder.services;

import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;

public interface ActorsCountService {
    public ActorCountsDto calculateTotals(final ActorsDto actors);
    public long countActors(final ActorsDto actors);
    public long countActorsWithUpcomingMovies(final ActorsDto actors);
    public long countActorsWithRecentMovies(final ActorsDto actors);
    public long countActorsWithSubscriptions(final ActorsDto actors);
}
