package org.robbins.moviefinder.services;

import org.robbins.moviefinder.entities.User;

import info.movito.themoviedbapi.model.people.PersonCredits;

public interface MovieFilterinService {
    public PersonCredits filterByUpcoming(final PersonCredits personCredits);
    public PersonCredits filterByRecent(final PersonCredits personCredits);
    public PersonCredits filterBySubscriptions(final PersonCredits personCredits, final User user);
}
