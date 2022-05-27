package org.robbins.moviefinder.services.tmdb.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.robbins.moviefinder.services.tmdb.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbPeople.PersonResultsPage;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;
import info.movito.themoviedbapi.model.people.PersonPeople;

@Service
public class PersonServiceImpl implements PersonService {
    final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    final TmdbApi tmdbApi;
    final Integer popularPageNumber = 1;

    public PersonServiceImpl(final TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @Override
    @Cacheable("personSearch")
    public List<Person> searchPersons(String personName) {
        return tmdbApi.getSearch().searchPerson(personName, false, 0).getResults();
    }

    @Override
    @Cacheable("persons")
    public PersonPeople findPerson(Long personId) {
        return tmdbApi.getPeople().getPersonInfo((Math.toIntExact(personId)));
    }

    @Override
    @Cacheable("movies")
    public PersonCredits findPersonMovieCredits(Long personId) {
        final PersonCredits fullCredits = tmdbApi.getPeople().getCombinedPersonCredits((Math.toIntExact(personId)));
        final PersonCredits castCredits = removeCrewCredits(fullCredits);
        final PersonCredits movieCredits = filterForMovies(castCredits);
        final PersonCredits sortedCredits = sortByPopularityAsc(movieCredits);

        return sortedCredits;
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

    @Override
    @Cacheable("popular")
    public PersonResultsPage findPopularPeople() {
        final PersonResultsPage popularPeople = tmdbApi.getPeople().getPersonPopular(popularPageNumber);
        final List<Person> filteredPeople = popularPeople.getResults()
                .stream()
                .collect(Collectors.toList());
        popularPeople.setResults(filteredPeople);

        return popularPeople;
    }
}
