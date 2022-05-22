package org.robbins.moviefinder.services.impl;

import java.util.Comparator;
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
import org.robbins.moviefinder.dtos.MovieDto;
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

import info.movito.themoviedbapi.model.people.PersonCredit;

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
        final ActorsDto actorsWithMovieCounts = findMyActorsWithCounts(user);
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

    private ActorsDto findMyActorsWithCounts(final User user) {
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
        return actorService.findActors(actorIds);
    }

    private ActorsDto findMyActorsWithMoviesAndCount(final Filters filter, final User user) {
        final List<ActorAlert> actorAlerts = actorAlertRepository.findByUser(user);

        final ActorsDto myActors = new ActorsDto();

        final List<ActorDto> actors = actorAlerts
                .stream()
                .map(actorAlert -> actorService.findActorWithMovies(actorAlert.getActorId(), Optional.of(filter),
                        Optional.of(user)))
                .collect(Collectors.toList());

        myActors.getActors().addAll(actors);
        myActors.getActors()
                .forEach(actor -> actor.setMovieCounts(
                        actorMovieCountService.findActorMovieCounts(actor.getActorId(), Optional.of(user))));
        myActors.setMovieCounts(calculateMovieCounts(myActors));

        return myActors;
    }

    @Override
    public ActorCountsDto findMyActorCounts(String userEmail) {
        final User user = userService.findByEmailUser(userEmail).get();
        final ActorsDto actorsWithMovieCounts = findMyActorsWithCounts(user);
        final ActorCountsDto actorCounts = actorCountService.calculateTotals(actorsWithMovieCounts);
        return actorCounts;
    }

    @Override
    public MoviesDto findMyMovies(String userEmail, final Filters filter) {
        final User user = userService.findByEmailUser(userEmail).get();

        final ActorsDto actors = findMyActorsWithMoviesAndCount(filter, user);

        final MoviesDto myMovies = new MoviesDto();
        myMovies.setMovieCounts(actors.getMovieCounts());
        myMovies.setMovies(addMoviesAndActors(actors));

        myMovies.getMovies()
                .sort(Comparator.comparing((MovieDto movie) -> movie.getCredit().getReleaseDate()).reversed());

        return myMovies;
    }

    private List<MovieDto> addMoviesAndActors(final ActorsDto actors) {
        final List<MovieDto> uniqueMovies = addUniqueMoviesForActors(actors);
        final List<MovieDto> moviesWithActors = addActorsForEachMovie(uniqueMovies, actors);
        return moviesWithActors;
    }

    private List<MovieDto> addUniqueMoviesForActors(final ActorsDto actors) {
        return actors.getActors()
                .stream()
                .map(actor -> actor.getMovieCredits().getCast())
                .flatMap(cast -> cast.stream())
                .map(credit -> convertCreditToMovieDto(credit))
                .distinct()
                .collect(Collectors.toList());
    }

    private List<MovieDto> addActorsForEachMovie(List<MovieDto> movies, final ActorsDto actors) {
        List<MovieDto> moviesWithActors = movies
            .stream()
            .map(movie -> addActorsForMovie(movie, actors))
            .collect(Collectors.toList());

        List<MovieDto> moviesWithCleanedActors = moviesWithActors
            .stream()
            .map(movie -> cleanActorCreditsFromMovie(movie))
            .collect(Collectors.toList());

        return moviesWithCleanedActors;
    }

    private MovieDto cleanActorCreditsFromMovie(final MovieDto movie) {
        movie.getActors()
            .forEach(actor -> actor.setMovieCredits(null));

        return movie;
    }

    private MovieDto addActorsForMovie(final MovieDto movie, final ActorsDto actors) {
        final List<ActorDto> actorsInMovie = actors.getActors()
            .stream()
            .filter(actor -> isActorInMovie(actor, movie.getCredit()))
            .collect(Collectors.toList());

        movie.setActors(actorsInMovie);
        return movie;
    }

    private boolean isActorInMovie(final ActorDto actor, final PersonCredit credit) {
        return actor.getMovieCredits().getCast()
            .stream()
            .anyMatch(personCredit -> personCredit.getId() == credit.getId());
    }

    private MovieDto convertCreditToMovieDto(final PersonCredit credit) {
        final MovieDto movie = new MovieDto();
        movie.setCredit(credit);
        return movie;
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
