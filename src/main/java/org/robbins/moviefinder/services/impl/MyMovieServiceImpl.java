package org.robbins.moviefinder.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.MovieDto;
import org.robbins.moviefinder.dtos.MoviesDto;
import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.services.ActorAlertService;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.MyMovieService;
import org.robbins.moviefinder.services.counting.ActorMovieCountService;
import org.robbins.moviefinder.services.counting.ActorsCountService;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.people.PersonCredit;

@Service
public class MyMovieServiceImpl implements MyMovieService {
    private final ActorAlertService actorAlertService;
    private final ActorService actorService;
    private final ActorsCountService actorCountService;
    private final ActorMovieCountService movieCountService;

    public MyMovieServiceImpl(final ActorsCountService actorCountService,
            final ActorService actorService,
            final ActorAlertService actorAlertService,
            final ActorMovieCountService movieCountService) {
        this.actorAlertService = actorAlertService;
        this.actorService = actorService;
        this.actorCountService = actorCountService;
        this.movieCountService = movieCountService;
    }

    @Override
    public MoviesDto findMyMovies(final User user, final Optional<MovieFilter> filter) {
        final List<ActorAlert> followedActors = actorAlertService.findActorAlerts(user);
        final ActorsDto actors = findMyActorsWithMoviesAndCount(followedActors, filter, user);

        final MoviesDto myMovies = new MoviesDto();
        myMovies.setActorCounts(actorCountService.calculateTotals(actors));
        myMovies.setMovieCounts(actors.getMovieCounts());
        myMovies.setMovies(addMoviesAndActors(actors));

        myMovies.getMovies()
                .sort(Comparator.comparing((MovieDto movie) -> movie.getCredit().getReleaseDate()).reversed());

        return myMovies;
    }

    private List<MovieDto> addMoviesAndActors(final ActorsDto actors) {
        final List<MovieDto> uniqueMovies = addUniqueMoviesForActors(actors);
        final List<MovieDto> sanitizedReleaseDates = sanitizeReleaseDates(uniqueMovies);
        final List<MovieDto> moviesWithActors = addActorsForEachMovie(sanitizedReleaseDates, actors);
        return moviesWithActors;
    }

    private ActorsDto findMyActorsWithMoviesAndCount(List<ActorAlert> followedActors,
            final Optional<MovieFilter> filter, final User user) {

        final ActorsDto myActors = new ActorsDto();

        final List<ActorDto> actors = followedActors
                .stream()
                .map(actorAlert -> actorService.findActorWithMovies(actorAlert.getActorId(), filter,
                        Optional.empty(), Optional.of(user)))
                .collect(Collectors.toList());

        myActors.getActors().addAll(actors);
        myActors.getActors()
                .forEach(actor -> actor.setMovieCounts(
                        movieCountService.findActorMovieCounts(actor.getActorId(), Optional.of(user))));
        myActors.setMovieCounts(movieCountService.calculateMovieCounts(myActors));

        return myActors;
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

    private List<MovieDto> sanitizeReleaseDates(final List<MovieDto> movies) {
        final List<MovieDto> sanitizedMovies = movies
                .stream()
                .map(movie -> sanitizeReleaseDate(movie))
                .collect(Collectors.toList());

        return sanitizedMovies;
    }

    private MovieDto sanitizeReleaseDate(final MovieDto movie) {
        final String sanitizedReleaseDate = movie.getCredit().getReleaseDate() != null
                ? movie.getCredit().getReleaseDate()
                : "";
        movie.getCredit().setReleaseDate(sanitizedReleaseDate);
        return movie;
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
}