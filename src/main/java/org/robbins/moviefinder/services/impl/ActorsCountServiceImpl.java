package org.robbins.moviefinder.services.impl;

import java.util.List;

import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.ActorsCountService;
import org.robbins.moviefinder.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class ActorsCountServiceImpl implements ActorsCountService {

    private final UserService userService;

    public ActorsCountServiceImpl(final UserService userService) {
        this.userService = userService;
    }

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
                .filter(actor -> actorHasMoviesMatchingUserSubscriptions(actor, user))
                .count();
        return Math.toIntExact(subscriptionCount);
    }

    private boolean actorHasMoviesMatchingUserSubscriptions(final ActorDto actor, final User user) {
        List<String> userSubscriptions = userService.convertStreamingServices(user);

        return actor.getMovieCounts().getSubscriptions()
                .stream()
                .anyMatch(subscription -> userSubscriptions.contains(subscription.getSubcriptionService()));
    }
}
