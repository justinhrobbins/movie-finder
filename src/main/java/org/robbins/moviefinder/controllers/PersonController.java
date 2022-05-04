package org.robbins.moviefinder.controllers;

import java.util.List;

import org.robbins.moviefinder.dtos.ActorMovieCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.services.ActorService;
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
    private final ActorService actorService;

    public PersonController(final PersonService personService, final ActorService actorService) {
        this.personService = personService;
        this.actorService = actorService;
    }

    @GetMapping("/find")
    public List<Person> findPerson(@RequestParam("name") final String personName) {
        return personService.searchPersons(personName);
    }

    @GetMapping("/{personId}")
    public PersonPeople getPersonById(@PathVariable("personId") final Long personId) {
        return personService.findPerson(personId);
    }

    @GetMapping("/{personId}/movies")
    public PersonCredits getMoviesForPerson(@PathVariable("personId") final Long personId) {
        return personService.findPersonMovieCredits(personId);
    }

    @GetMapping("/{actorId}/movie/counts")
    public ActorMovieCountsDto getMovieCountsForActor(@PathVariable("actorId") final Long actorId) {
        return actorService.findActorMovieCounts(actorId);
    }

    @GetMapping("/popular")
    public ActorsDto getPopluar() {
        return actorService.findPopularActors();
    }
}
