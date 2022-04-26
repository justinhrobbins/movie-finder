package org.robbins.moviefinder.services;

import java.util.List;

import org.robbins.moviefinder.dtos.ActorDetailsDto;

import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredits;
import info.movito.themoviedbapi.model.people.PersonPeople;

public interface PersonService {
    public List<Person> searchPersons(final String personName);
    public PersonPeople findPersonDetails(final int personId);
    public PersonCredits findPersonMovieCredits(final int personId);
    public ActorDetailsDto findActorDetails(final int actorId);
}
