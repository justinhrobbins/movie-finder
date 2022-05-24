package org.robbins.moviefinder.services.filtering;

import java.util.Optional;

import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.MovieFilter;

import info.movito.themoviedbapi.model.people.PersonCredits;

public interface MovieFilteringService {
    public PersonCredits filter(final PersonCredits personCredits, final MovieFilter filter, final Optional<User> user);
    public PersonCredits filterByUpcoming(final PersonCredits personCredits);
    public PersonCredits filterByRecent(final PersonCredits personCredits);
    public PersonCredits filterBySubscriptions(final PersonCredits personCredits, final Optional<User> user);
}
