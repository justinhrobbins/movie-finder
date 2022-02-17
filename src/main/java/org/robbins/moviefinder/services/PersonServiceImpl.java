package org.robbins.moviefinder.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public PersonPeople findPersonDetails(int personId) {
        return tmdbApi.getPeople().getPersonInfo(personId);
    }

    @Override
    public PersonCredits findPersonMovieCredits(int personId) {
        final PersonCredits fullCredits = tmdbApi.getPeople().getCombinedPersonCredits(personId);
        final PersonCredits castCredits = removeCrewCredits(fullCredits);
        final PersonCredits movieCredits = filterForMovies(castCredits);
        final PersonCredits sortedCredits = sortByReleaseDateDesc(movieCredits);

        return sortedCredits;
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

    private PersonCredits sortByReleaseDateDesc(final PersonCredits credits) {
        credits.getCast().sort(Comparator.comparing(credit -> credit.getReleaseDate(),
                Comparator.nullsLast(Comparator.reverseOrder())));
        return credits;
    }
}
