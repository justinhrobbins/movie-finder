package org.robbins.moviefinder.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.robbins.moviefinder.dtos.ActorAlertDto;
import org.robbins.moviefinder.dtos.ActorAlertsDto;
import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.Filters;
import org.robbins.moviefinder.dtos.MovieCountsDto;
import org.robbins.moviefinder.dtos.MoviesDto;
import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.repositories.ActorAlertRepository;
import org.robbins.moviefinder.services.ActorAlertService;
import org.robbins.moviefinder.services.ActorMovieCountService;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.ActorsCountService;
import org.robbins.moviefinder.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActorAlertServiceImpl implements ActorAlertService {
    final Logger logger = LoggerFactory.getLogger(ActorAlertService.class);

    private final UserService userService;
    private final ActorService actorService;
    private final ActorAlertRepository actorAlertRepository;
    private final ActorsCountService actorCountService;
    private final ActorMovieCountService actorMovieCountService;

    public ActorAlertServiceImpl(final UserService userService,
            final ActorAlertRepository actorAlertRepository,
            final ActorService actorService,
            final ActorsCountService actorCountService,
            final ActorMovieCountService actorMovieCountService) {

        this.userService = userService;
        this.actorAlertRepository = actorAlertRepository;
        this.actorService = actorService;
        this.actorCountService = actorCountService;
        this.actorMovieCountService = actorMovieCountService;
    }

    @Override
    public ActorAlertsDto findMyActorAlerts(String userEmail, final Filters filter) {
        final User user = userService.findByEmailUser(userEmail).get();
        final ActorAlertsDto actorAlerts = new ActorAlertsDto();
        final List<ActorAlert> entities = actorAlertRepository.findByUser(user);

        entities.forEach(entity -> {
            final ActorAlertDto actorAlert = new ActorAlertDto(entity.getActorId());
            actorAlerts.getActorAlerts().add(actorAlert);
        });

        return actorAlerts;
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
        final ActorsDto actorsWithMovieCounts = findActorsWithCounts(user);
        actorsWithMovieCounts.setActorCounts(actorCountService.calculateTotals(actorsWithMovieCounts));
        actorsWithMovieCounts.setMovieCounts(calculateMovieCounts(actorsWithMovieCounts));
        return actorsWithMovieCounts;
    }

    private ActorsDto populateActorMovieCounts(final ActorsDto actors, final User user) {
        actors.getActors()
                .forEach(actor -> {
                    actor.setMovieCounts(
                            actorMovieCountService.findActorMovieCounts(actor.getActorId(), Optional.of(user)));
                });

        return actors;
    }

    private ActorsDto findActorsWithCounts(final User user) {
        final ActorsDto actors = findMyActorsWithoutCounts(user);
        final ActorsDto actorsWithMovieCounts = populateActorMovieCounts(actors, user);
        return actorsWithMovieCounts;
    }

    private ActorsDto findMyActorsWithoutCounts(final User user) {
        List<ActorAlert> actorAlerts = actorAlertRepository.findByUser(user);

        List<Long> actorIds = actorAlerts
                .stream()
                .map(ActorAlert::getActorId)
                .collect(Collectors.toList());
        return actorService.findActors(actorIds, user);
    }

    @Override
    public ActorCountsDto findMyActorCounts(String userEmail) {
        final User user = userService.findByEmailUser(userEmail).get();
        final ActorsDto actorsWithMovieCounts = findActorsWithCounts(user);
        final ActorCountsDto actorCounts = actorCountService.calculateTotals(actorsWithMovieCounts);
        return actorCounts;
    }

    @Override
    public MoviesDto findMyMovies(String userEmail, final Filters filter) {
        final User user = userService.findByEmailUser(userEmail).get();
        List<ActorAlert> actorAlerts = actorAlertRepository.findByUser(user);

        final MoviesDto movies = new MoviesDto();

        actorAlerts
                .parallelStream()
                .forEach(actorAlert -> {
                    final ActorDto actor = actorService.findActorWithMovies(actorAlert.getActorId(), filter, user);
                    actor.setMovieCounts(
                            actorMovieCountService.findActorMovieCounts(actor.getActorId(), Optional.of(user)));
                    if (actor.getMovieCredits().getCast().size() > 0) {
                        movies.getActors().getActors().add(actor);
                    }
                });

        final MovieCountsDto movieCounts = calculateMovieCounts(movies.getActors());
        movies.setMovieCounts(movieCounts);
        return movies;
    }

    private MovieCountsDto calculateMovieCounts(final ActorsDto actorsDto) {
        long totalMovies = actorsDto.getActors()
                .stream()
                .mapToLong(actor -> actor.getMovieCounts().getTotalMovies())
                .sum();

        long upcomingMovies = actorsDto.getActors()
                .stream()
                .mapToLong(actor -> actor.getMovieCounts().getUpcomingMovies())
                .sum();

        long recentMovies = actorsDto.getActors()
                .stream()
                .mapToLong(actor -> actor.getMovieCounts().getRecentMovies())
                .sum();

        long moviesOnSubscriptions = actorsDto.getActors()
                .stream()
                .mapToLong(actor -> actor.getMovieCounts().getMoviesOnSubscriptions())
                .sum();

        return new MovieCountsDto(totalMovies, upcomingMovies, recentMovies,
                moviesOnSubscriptions);
    }
}
