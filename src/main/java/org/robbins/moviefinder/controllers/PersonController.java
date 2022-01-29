package org.robbins.moviefinder.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredits;

@RestController
@RequestMapping("person")
public class PersonController {
    
    @GetMapping("/find")
    public List<Person> findPerson(@RequestParam("name") final String personName) {
        final TmdbApi tmdbApi = new TmdbApi("282db9810c3f6938665eb39b7b659797");
        final List<Person> persons = tmdbApi.getSearch().searchPerson(personName, false, 0).getResults();

        // final Person firstPerson = persons.get(0);
        return persons;
    }

    @GetMapping("/{personId}/movies")
    public PersonCredits getMoviesForPerson(@PathVariable("personId") final int personId) {
        final TmdbApi tmdbApi = new TmdbApi("282db9810c3f6938665eb39b7b659797");
        final PersonCredits credits = tmdbApi.getPeople().getCombinedPersonCredits(personId);
        return credits;
    }
}
