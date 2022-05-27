package org.robbins.moviefinder.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.MovieCountsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.enums.MovieSort;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.UserService;
import org.robbins.moviefinder.services.counting.ActorMovieCountService;
import org.robbins.moviefinder.services.tmdb.PersonService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredits;

@RestController
@CrossOrigin
@RequestMapping("actors")
public class ActorController extends AbstractController {
    private final PersonService personService;
    private final ActorService actorService;
    private final UserService userService;
    private final ActorMovieCountService movieCountService;

    public ActorController(final PersonService personService,
            final ActorService actorService, final ActorMovieCountService movieCountService,
            final UserService userService) {
        this.personService = personService;
        this.actorService = actorService;
        this.movieCountService = movieCountService;
        this.userService = userService;
    }

    @GetMapping("/find")
    public List<Person> findPerson(@RequestParam("name") final String personName) {
        return personService.searchPersons(personName);
    }

    @GetMapping("/{actorId}")
    public ActorDto getPersonById(@PathVariable("actorId") final Long personId) {
        return actorService.findActor(personId);
    }

    @GetMapping("/{actorId}/movies")
    public PersonCredits getMoviesForPerson(@RequestParam(name = "filter", required = false) final MovieFilter filter,
            @RequestParam(name = "sort", required = false) final MovieSort sort,
            @PathVariable("actorId") final Long actorId,
            final Principal principal) {

        final Optional<User> user = extractUserFromPrincipal(principal);
        Optional<MovieFilter> optionalFilter = filter != null ? Optional.of(filter) : Optional.empty();
        Optional<MovieSort> optionalSort = sort != null ? Optional.of(sort) : Optional.empty();

        final ActorDto actor = actorService.findActorWithMovies(actorId, optionalFilter, optionalSort, user);
        return actor.getMovieCredits();
    }

    @GetMapping("/{actorId}/movies/counts")
    public MovieCountsDto getMovieCountsForActor(@PathVariable("actorId") final Long actorId,
            final Principal principal) {
        final Optional<User> user = extractUserFromPrincipal(principal);
        return movieCountService.findActorMovieCounts(actorId, user);
    }

    @GetMapping("/popular")
    public ActorsDto getPopluar() {
        return actorService.findPopularActors();
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
