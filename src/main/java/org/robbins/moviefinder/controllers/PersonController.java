package org.robbins.moviefinder.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredits;

@RestController
@CrossOrigin
@RequestMapping("person")
public class PersonController {
    final TmdbApi tmdbApi;
    
    public PersonController(final TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @GetMapping("/find")
    public List<Person> findPerson(@RequestParam("name") final String personName) {        
        final List<Person> persons = tmdbApi.getSearch().searchPerson(personName, false, 0).getResults();
        return persons;
    }

    @GetMapping("/{personId}/movies")
    public PersonCredits getMoviesForPerson(@PathVariable("personId") final int personId) {
        final PersonCredits credits = tmdbApi.getPeople().getCombinedPersonCredits(personId);
        return credits;
    }
}
