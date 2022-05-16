package org.robbins.moviefinder.services.impl;

import java.util.Optional;

import org.robbins.moviefinder.dtos.MovieCountsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.ActorMovieCountService;
import org.robbins.moviefinder.services.FlatrateProviderService;
import org.robbins.moviefinder.services.MovieFilterinService;
import org.robbins.moviefinder.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.people.PersonCredits;

@Service
public class ActorMovieCountServiceImpl implements ActorMovieCountService {
    final Logger logger = LoggerFactory.getLogger(ActorMovieCountServiceImpl.class);

    private final PersonService personService;
    private final FlatrateProviderService flatrateProviderService;
    private final MovieFilterinService movieFilterService;

    public ActorMovieCountServiceImpl(final PersonService personService,
            final MovieFilterinService movieFilterService, final FlatrateProviderService flatrateProviderService) {
        this.personService = personService;
        this.flatrateProviderService = flatrateProviderService;
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
            moviesOnSubscriptions = countMovieSubscriptions(personCredits, user.get());
        }

        return new MovieCountsDto(totalMovies, upcomingMovies, recentMovies, moviesOnSubscriptions);
    }

    private long countMovieSubscriptions(final PersonCredits personCredits, final User user) {
        return personCredits.getCast()
                .stream()
                .filter(credit -> flatrateProviderService.movieIsOnSubscription(credit.getId(), user))
                .count();
    }
}
