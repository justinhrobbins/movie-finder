package org.robbins.moviefinder.services.impl;

import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.services.ActorsCountService;
import org.springframework.stereotype.Service;

@Service
public class ActorsCountServiceImpl implements ActorsCountService {

    public ActorsCountServiceImpl() {
    }

    @Override
    public ActorCountsDto calculateTotals(ActorsDto actors) {
        final ActorCountsDto actorCounts = new ActorCountsDto();
        actorCounts.setActorCount(countActors(actors));
        actorCounts.setUpcomingMovieCount(countActorsWithUpcomingMovies(actors));
        actorCounts.setRecentMovieCount(countActorsWithRecentMovies(actors));
        actorCounts.setSubscriptionCount(countActorsWithSubscriptions(actors));

        return actorCounts;
    }

    @Override
    public long countActors(final ActorsDto actors) {
        return actors.getActors().size();
    }

    @Override
    public long countActorsWithUpcomingMovies(final ActorsDto actors) {
        long upcomingMovieCount = actors.getActors()
                .parallelStream()
                .filter(actor -> actor.getMovieCounts().getUpcomingMovies() > 0)
                .count();

        return Math.toIntExact(upcomingMovieCount);
    }

    @Override
    public long countActorsWithRecentMovies(final ActorsDto actors) {
        long recentMovieCount = actors.getActors()
                .parallelStream()
                .filter(actor -> actor.getMovieCounts().getRecentMovies() > 0)
                .count();

        return Math.toIntExact(recentMovieCount);
    }

    @Override
    public long countActorsWithSubscriptions(final ActorsDto actors) {
        long subscriptionCount = actors.getActors()
                .parallelStream()
                .filter(actor -> actor.getMovieCounts().getMoviesOnSubscriptions() > 0)
                .count();
        return Math.toIntExact(subscriptionCount);
    }
}
