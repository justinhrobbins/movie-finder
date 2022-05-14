package org.robbins.moviefinder.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.MovieCountsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.ActorMovieCountService;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.PersonService;
import org.robbins.moviefinder.services.UserService;
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
    public PersonCredits getMoviesForPerson(@PathVariable("actorId") final Long personId) {
        return personService.findPersonMovieCredits(personId);
    }

    @GetMapping("/{actorId}/movies/counts")
    public MovieCountsDto getMovieCountsForActor(@PathVariable("actorId") final Long actorId, final Principal principal) {
        final Optional<User> user = findUser(principal);
        return movieCountService.findActorMovieCounts(actorId, user);
    }

    private Optional<User> findUser(final Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }

        final String userEmail = extractUserEmailFromPrincipal(principal);
        return userService.findByEmailUser(userEmail);
    }

    @GetMapping("/popular")
    public ActorsDto getPopluar() {
        return actorService.findPopularActors();
    }
}
