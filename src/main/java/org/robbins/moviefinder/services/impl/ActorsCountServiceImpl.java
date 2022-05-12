package org.robbins.moviefinder.services.impl;

import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.ActorsCountService;
import org.springframework.stereotype.Service;

@Service
public class ActorsCountServiceImpl implements ActorsCountService {

    public ActorsCountServiceImpl() {}

    @Override
    public ActorCountsDto calculateTotals(ActorsDto actors, User user) {
        final ActorCountsDto actorCounts = new ActorCountsDto();
        actorCounts.setActorCount(calculateActorAlertsCount(actors));
        actorCounts.setUpcomingMovieCount(calculateUpcomingMovieCount(actors));
        actorCounts.setRecentMovieCount(calculateRecentMovieCount(actors));
        actorCounts.setSubscriptionCount(calculateSubscriptionCount(actors, user));

        return actorCounts;
    }

    private int calculateActorAlertsCount(final ActorsDto actors) {
        return actors.getActors().size();
    }

    private int calculateUpcomingMovieCount(final ActorsDto actors) {
        long upcomingMovieCount = actors.getActors()
                .parallelStream()
                .filter(actor -> actor.getMovieCounts().getUpcomingMovies() > 0)
                .count();

        return Math.toIntExact(upcomingMovieCount);
    }

    private int calculateRecentMovieCount(final ActorsDto actors) {
        long recentMovieCount = actors.getActors()
                .parallelStream()
                .filter(actor -> actor.getMovieCounts().getRecentMovies() > 0)
                .count();

        return Math.toIntExact(recentMovieCount);
    }

    private int calculateSubscriptionCount(final ActorsDto actors, final User user) {
        long subscriptionCount = actors.getActors()
                .stream()
                .filter(actor -> actor.getMovieCounts().getMoviesOnSubscriptions() > 0)
                .count();
        return Math.toIntExact(subscriptionCount);
    }
}
