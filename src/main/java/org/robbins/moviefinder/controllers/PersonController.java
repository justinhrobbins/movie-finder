package org.robbins.moviefinder.controllers;

import java.util.List;

import org.robbins.moviefinder.dtos.ActorAlertsDto;
import org.robbins.moviefinder.dtos.ActorDetailsDto;
import org.robbins.moviefinder.services.PersonService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredits;
import info.movito.themoviedbapi.model.people.PersonPeople;

@RestController
@CrossOrigin
@RequestMapping("person")
public class PersonController {
    private final PersonService personService;

    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/find")
    public List<Person> findPerson(@RequestParam("name") final String personName) {
        return personService.searchPersons(personName);
    }

    @GetMapping("/{personId}")
    public PersonPeople getPersonDetails(@PathVariable("personId") final int personId) {
        return personService.findPersonDetails(personId);
    }

    @GetMapping("/{personId}/movies")
    public PersonCredits getMoviesForPerson(@PathVariable("personId") final int personId) {
        return personService.findPersonMovieCredits(personId);
    }

    @GetMapping("/{actorId}/details")
    public ActorDetailsDto getDetailsForActor(@PathVariable("actorId") final int actorId) {
        return personService.findActorDetails(actorId);
    }

    @GetMapping("/popular")
    public ActorAlertsDto getPopluar() {
        return personService.findPopularActors();
    }
}
