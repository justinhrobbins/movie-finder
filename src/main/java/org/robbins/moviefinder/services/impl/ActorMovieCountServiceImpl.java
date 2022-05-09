package org.robbins.moviefinder.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.robbins.moviefinder.dtos.ActorMovieCountsDto;
import org.robbins.moviefinder.dtos.ActorMovieSubscriptionCountsDto;
import org.robbins.moviefinder.services.ActorMovieCountService;
import org.robbins.moviefinder.services.MovieService;
import org.robbins.moviefinder.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.WatchProviders;
import info.movito.themoviedbapi.model.WatchProviders.Provider;
import info.movito.themoviedbapi.model.WatchProviders.Results.US;
import info.movito.themoviedbapi.model.people.PersonCredits;

@Service
public class ActorMovieCountServiceImpl implements ActorMovieCountService {
    final Logger logger = LoggerFactory.getLogger(ActorMovieCountServiceImpl.class);

    private final PersonService personService;
    private final MovieService movieService;

    public ActorMovieCountServiceImpl(final PersonService personService, final MovieService movieService) {
        this.personService = personService;
        this.movieService = movieService;
    }

    @Override
    public ActorMovieCountsDto findActorMovieCounts(final Long actorId) {
        final PersonCredits personCredits = personService.findPersonMovieCredits(actorId);

        final LocalDate today = LocalDate.now();

        final long totalMovies = personCredits.getCast().size();

        final long upcomingMovies = personCredits.getCast()
                .stream()
                .filter(credit -> !(credit.getReleaseDate() == null))
                .filter(credit -> credit.getReleaseDate().length() > 0)
                .filter(credit -> LocalDate.parse(credit.getReleaseDate()).isAfter(today))
                .count();

        final long recentMovies = personCredits.getCast()
                .stream()
                .filter(credit -> !(credit.getReleaseDate() == null))
                .filter(credit -> credit.getReleaseDate().length() > 0)
                .filter(credit -> isWithinRecentRange(LocalDate.parse(credit.getReleaseDate())))
                .count();

        final List<ActorMovieSubscriptionCountsDto> subscriptionCounts = findSubscriptions(personCredits);

        return new ActorMovieCountsDto(totalMovies, upcomingMovies, recentMovies, subscriptionCounts);
    }

    private boolean isWithinRecentRange(LocalDate date) {
        final LocalDate startOfRange = LocalDate.now().minusMonths(12);
        final LocalDate endOfRange = LocalDate.now();

        return !(date.isBefore(startOfRange) || date.isAfter(endOfRange));
    }

    private List<ActorMovieSubscriptionCountsDto> findSubscriptions(final PersonCredits personCredits) {
        List<ActorMovieSubscriptionCountsDto> subscriptionCounts = new ArrayList<>();

        personCredits.getCast()
                .stream()
                .forEach(credit -> {
                    populateFlatrateProviders(credit.getId(), subscriptionCounts);
                });

        return subscriptionCounts;
    }

    private void populateFlatrateProviders(int movieId,
            final List<ActorMovieSubscriptionCountsDto> subscriptionCounts) {
        final MovieDb movie = movieService.findMovieWatchProviders(movieId, "en");
        final WatchProviders watchProviders = movie.getWatchProviders();
        final WatchProviders.Results results = watchProviders.getResults();
        final US us = results.getUS();
        if (us != null) {
            final List<Provider> flatRateProviders = us.getFlatrateProviders();
            if (flatRateProviders != null) {
                flatRateProviders
                        .stream()
                        .forEach(provider -> populateFlatrateProvider(provider, subscriptionCounts));
            }
        }
    }

    private synchronized void populateFlatrateProvider(final Provider provider,
            final List<ActorMovieSubscriptionCountsDto> subscriptionCounts) {

        final Optional<ActorMovieSubscriptionCountsDto> counts = subscriptionCounts
                .stream()
                .filter(subscriptionCount -> StringUtils.equalsIgnoreCase(subscriptionCount.getSubcriptionService(),
                        provider.getProviderName()))
                .findAny();

        if (counts.isPresent()) {
            final ActorMovieSubscriptionCountsDto actorMovieSubscriptionCount = counts.get();
            actorMovieSubscriptionCount.setMovieCount(actorMovieSubscriptionCount.getMovieCount() + 1);
        } else {
            final ActorMovieSubscriptionCountsDto actorMovieSubscriptionCount = new ActorMovieSubscriptionCountsDto(
                    provider.getProviderName(), 1);
            subscriptionCounts.add(actorMovieSubscriptionCount);
        }
    }
}
