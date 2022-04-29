package org.robbins.moviefinder.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.robbins.moviefinder.dtos.ActorDetailsDto;
import org.robbins.moviefinder.dtos.ActorMovieSubscriptionCounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.WatchProviders;
import info.movito.themoviedbapi.model.WatchProviders.Provider;
import info.movito.themoviedbapi.model.WatchProviders.Results.US;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;
import info.movito.themoviedbapi.model.people.PersonPeople;

@Service
public class PersonServiceImpl implements PersonService {
    Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    final TmdbApi tmdbApi;
    final MovieService movieService;

    public PersonServiceImpl(final TmdbApi tmdbApi, final MovieService movieService) {
        this.tmdbApi = tmdbApi;
        this.movieService = movieService;
    }

    @Override
    public List<Person> searchPersons(String personName) {
        return tmdbApi.getSearch().searchPerson(personName, false, 0).getResults();
    }

    @Override
    @Cacheable("persons")
    public PersonPeople findPersonDetails(int personId) {
        return tmdbApi.getPeople().getPersonInfo(personId);
    }

    @Override
    @Cacheable("movies")
    public PersonCredits findPersonMovieCredits(int personId) {
        final PersonCredits fullCredits = tmdbApi.getPeople().getCombinedPersonCredits(personId);
        final PersonCredits castCredits = removeCrewCredits(fullCredits);
        final PersonCredits movieCredits = filterForMovies(castCredits);
        final PersonCredits sortedCredits = sortByPopularityAsc(movieCredits);

        return sortedCredits;
    }

    @Override
    @Cacheable("details")
    public ActorDetailsDto findActorDetails(final int actorId) {
        final PersonCredits personCredits = findPersonMovieCredits(actorId);
        final LocalDate today = LocalDate.now();

        final long totalMovies = personCredits.getCast().size();

        final long upcomingMovies = personCredits.getCast()
                .parallelStream()
                .filter(credit -> !(credit.getReleaseDate() == null))
                .filter(credit -> credit.getReleaseDate().length() > 0)
                .filter(credit -> LocalDate.parse(credit.getReleaseDate()).isAfter(today))
                .count();

        final long recentMovies = personCredits.getCast()
                .parallelStream()
                .filter(credit -> !(credit.getReleaseDate() == null))
                .filter(credit -> credit.getReleaseDate().length() > 0)
                .filter(credit -> isWithinRange(LocalDate.parse(credit.getReleaseDate())))
                .count();

        final List<ActorMovieSubscriptionCounts> subscriptionCounts = findSubscriptions(personCredits);

        return new ActorDetailsDto(totalMovies, upcomingMovies, recentMovies, subscriptionCounts);
    }

    private boolean isWithinRange(LocalDate date) {
        final LocalDate startOfRange = LocalDate.now().minusMonths(12);
        final LocalDate endOfRange = LocalDate.now();

        return !(date.isBefore(startOfRange) || date.isAfter(endOfRange));
    }

    private PersonCredits removeCrewCredits(final PersonCredits credits) {
        credits.setCrew(Collections.emptyList());
        return credits;
    }

    private PersonCredits filterForMovies(final PersonCredits credits) {
        final List<PersonCredit> filteredCredits = credits.getCast()
                .parallelStream()
                .filter(castCredit -> castCredit.getMediaType().equals("movie"))
                .collect(Collectors.toList());

        credits.setCast(filteredCredits);
        return credits;
    }

    private PersonCredits sortByPopularityAsc(final PersonCredits credits) {
        credits.getCast().sort(Comparator.comparing(credit -> credit.getPopularity(),
                Comparator.nullsLast(Comparator.reverseOrder())));
        return credits;
    }

    private List<ActorMovieSubscriptionCounts> findSubscriptions(final PersonCredits personCredits) {
        List<ActorMovieSubscriptionCounts> subscriptionCounts = new ArrayList<>();

        personCredits.getCast()
                .parallelStream()
                .forEach(credit -> {
                    populateFlatrateProviders(credit.getId(), subscriptionCounts);
                });

        return subscriptionCounts;
    }

    private void populateFlatrateProviders(int movieId, final List<ActorMovieSubscriptionCounts> subscriptionCounts) {
        final MovieDb movie = movieService.findMovieWatchProviders(movieId, "en");
        final WatchProviders watchProviders = movie.getWatchProviders();
        final WatchProviders.Results results = watchProviders.getResults();
        final US us = results.getUS();
        if (us != null) {
            final List<Provider> flatRateProviders = us.getFlatrateProviders();
            if (flatRateProviders != null) {
                flatRateProviders
                        .parallelStream()
                        .forEach(provider -> populateFlatrateProvider(provider, subscriptionCounts));
            }
        }
    }

    private synchronized void populateFlatrateProvider(final Provider provider,
            final List<ActorMovieSubscriptionCounts> subscriptionCounts) {

        final Optional<ActorMovieSubscriptionCounts> counts = subscriptionCounts
                .parallelStream()
                .filter(subscriptionCount -> StringUtils.equalsIgnoreCase(subscriptionCount.getSubcriptionService(),
                        provider.getProviderName()))
                .findAny();

        if (counts.isPresent()) {
            final ActorMovieSubscriptionCounts actorMovieSubscriptionCount = counts.get();
            actorMovieSubscriptionCount.setMovieCount(actorMovieSubscriptionCount.getMovieCount() + 1);
        } else {
            final ActorMovieSubscriptionCounts actorMovieSubscriptionCount = new ActorMovieSubscriptionCounts(
                    provider.getProviderName(), 1);
            subscriptionCounts.add(actorMovieSubscriptionCount);
        }
    }
}
