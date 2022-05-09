package org.robbins.moviefinder.services;

import org.robbins.moviefinder.dtos.ActorMovieCountsDto;

import info.movito.themoviedbapi.model.people.PersonCredits;

public interface ActorMovieCountService {
    public ActorMovieCountsDto findActorMovieCounts(final Long actorId);
    public PersonCredits filterByUpcoming(final PersonCredits personCredits);
    public PersonCredits filterbyRecent(final PersonCredits personCredits);
}
