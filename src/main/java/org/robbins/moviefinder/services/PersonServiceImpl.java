package org.robbins.moviefinder.services;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.robbins.moviefinder.dtos.ActorDetailsDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;
import info.movito.themoviedbapi.model.people.PersonPeople;

@Service
public class PersonServiceImpl implements PersonService {
    final TmdbApi tmdbApi;

    public PersonServiceImpl(final TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
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
                .stream()
                .filter(credit -> !(credit.getReleaseDate() == null))
                .filter(credit -> credit.getReleaseDate().length() > 0)
                .filter(credit -> LocalDate.parse(credit.getReleaseDate()).isAfter(today))
                .count();

        final long recentMovies = personCredits.getCast()
                .stream()
                .filter(credit -> !(credit.getReleaseDate() == null))
                .filter(credit -> credit.getReleaseDate().length() > 0)
                .filter(credit -> isWithinRange(LocalDate.parse(credit.getReleaseDate())))
                .count();

        return new ActorDetailsDto(totalMovies, upcomingMovies, recentMovies);
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
        final List<PersonCredit> filteredCredits = credits.getCast().stream()
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
}
