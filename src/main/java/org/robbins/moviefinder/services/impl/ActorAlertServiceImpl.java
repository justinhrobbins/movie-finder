package org.robbins.moviefinder.services.impl;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.MoviesDto;
import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.repositories.ActorAlertRepository;
import org.robbins.moviefinder.services.ActorAlertService;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class ActorAlertServiceImpl implements ActorAlertService {

    private final UserService userService;
    private final ActorService actorService;
    private final ActorAlertRepository actorAlertRepository;

    public ActorAlertServiceImpl(final UserService userService, final ActorAlertRepository actorAlertRepository,
            final ActorService actorService) {
        this.userService = userService;
        this.actorAlertRepository = actorAlertRepository;
        this.actorService = actorService;
    }

    @Override
    public Boolean isUserFollowingActor(String userEmail, Long actorId) {
        final User user = userService.findByEmailUser(userEmail).get();

        Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);

        return actorAlert.isPresent() ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public void saveActorAlert(String userEmail, Long actorId) {
        final User user = userService.findByEmailUser(userEmail).get();
        actorAlertRepository.save(new ActorAlert(user, actorId));
    }

    @Override
    public void deleteActorAlert(final String userEmail, final Long actorId) {
        final User user = userService.findByEmailUser(userEmail).get();

        final Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);

        if (!actorAlert.isPresent()) {
            throw new RuntimeException("Actor Alert not found");
        } else {
            actorAlertRepository.delete(actorAlert.get());
            return;
        }
    }

    @Override
    public ActorsDto findAMyActors(final String userEmail) {
        final User user = userService.findByEmailUser(userEmail).get();

        List<ActorAlert> actorAlerts = actorAlertRepository.findByUser(user);
        final ActorsDto actorAlertsDto = convertActorAlertsToActors(actorAlerts, user);

        return actorAlertsDto;
    }

    private ActorsDto convertActorAlertsToActors(final List<ActorAlert> actorAlerts, final User user) {
        final ActorsDto actors = new ActorsDto();

        actorAlerts
                .parallelStream()
                .forEach(actorAlert -> {
                    final ActorDto actor = actorService.findByActorId(actorAlert.getActorId());
                    actors.getActors().add(actor);
                });

        final ActorsDto actorAlertsDtoWithTotals = calculateTotals(actors, user);
        return actorAlertsDtoWithTotals;
    }

    private ActorsDto calculateTotals(final ActorsDto actors, final User user) {
        actors.setActorCount(calculateActorAlertsCount(actors));
        actors.setUpcomingMovieCount(calculateUpcomingMovieCount(actors));
        actors.setRecentMovieCount(calculateRecentMovieCount(actors));
        actors.setSubscriptionCount(calculateSubscriptionCount(actors, user));

        return actors;
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

    @Override
    public MoviesDto findMyMovies(String userEmail) {
        final User user = userService.findByEmailUser(userEmail).get();
        List<ActorAlert> actorAlerts = actorAlertRepository.findByUser(user);

        final MoviesDto movies = new MoviesDto();

        actorAlerts
                .stream()
                .forEach(actorAlert -> {
                    final ActorDto actor = actorService.findActorWithMovies(actorAlert.getActorId());
                    movies.getMovies().addAll(actor.getMovieCredits().getCast());
                });

        return movies;
    }
}
