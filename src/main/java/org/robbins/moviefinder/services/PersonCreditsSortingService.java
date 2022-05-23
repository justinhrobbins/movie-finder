package org.robbins.moviefinder.services;

import org.robbins.moviefinder.enums.MovieSort;

import info.movito.themoviedbapi.model.people.PersonCredits;

public interface PersonCreditsSortingService {
    public PersonCredits sort(final PersonCredits credits, final MovieSort sort);
}
