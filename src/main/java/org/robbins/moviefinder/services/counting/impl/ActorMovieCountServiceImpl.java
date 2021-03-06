package org.robbins.moviefinder.services.counting.impl;

import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.MovieCountsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.counting.ActorMovieCountService;
import org.robbins.moviefinder.services.filtering.MovieFilteringService;
import org.robbins.moviefinder.services.tmdb.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.people.PersonCredits;

@Service
public class ActorMovieCountServiceImpl implements ActorMovieCountService {
    final Logger logger = LoggerFactory.getLogger(ActorMovieCountServiceImpl.class);

    private final PersonService personService;
    private final MovieFilteringService movieFilterService;

    public ActorMovieCountServiceImpl(final PersonService personService,
            final MovieFilteringService movieFilterService) {
        this.personService = personService;
        this.movieFilterService = movieFilterService;
    }

    @Override
    public MovieCountsDto findActorMovieCounts(final Long actorId, final Optional<User> user) {
        final PersonCredits personCredits = personService.findPersonMovieCredits(actorId);

        final long totalMovies = personCredits.getCast().size();

        final long upcomingMovies = movieFilterService.filterByUpcoming(personCredits).getCast().size();

        final long recentMovies = movieFilterService.filterByRecent(personCredits).getCast().size();

        long moviesOnSubscriptions = 0;
        if (user.isPresent()) {
            moviesOnSubscriptions = movieFilterService.filterBySubscriptions(personCredits, user).getCast()
                    .size();
        }

        return new MovieCountsDto(totalMovies, upcomingMovies, recentMovies, moviesOnSubscriptions);
    }

    @Override
    public MovieCountsDto calculateMovieCounts(final ActorsDto actorsDto) {
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
