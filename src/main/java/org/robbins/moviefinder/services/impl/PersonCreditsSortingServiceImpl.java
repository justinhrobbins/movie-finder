package org.robbins.moviefinder.services.impl;

import java.util.Comparator;

import org.robbins.moviefinder.enums.MovieSort;
import org.robbins.moviefinder.services.PersonCreditsSortingService;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;

@Service
public class PersonCreditsSortingServiceImpl implements PersonCreditsSortingService {

    @Override
    public PersonCredits sort(final PersonCredits credits, final MovieSort sort) {
        switch (sort) {
            case POPULARITY:
                return sortByPopularity(credits);
            case NEWEST:
                return sortByNewest(credits);
            case OLDEST:
                return sortByOldest(credits);
            default:
                return credits;
        }
    }

    private PersonCredits sortByPopularity(final PersonCredits credits) {
        credits.getCast().sort(Comparator.comparing((PersonCredit credit) -> credit.getPopularity()));
        return credits;
    }

    private PersonCredits sortByNewest(final PersonCredits credits) {
        credits.getCast().sort(Comparator.comparing((PersonCredit credit) -> credit.getReleaseDate()).reversed());
        return credits;
    }

    private PersonCredits sortByOldest(final PersonCredits credits) {
        credits.getCast().sort(Comparator.comparing((PersonCredit credit) -> credit.getReleaseDate()));
        return credits;
    }
}
