package org.robbins.moviefinder.controllers.secured;

import java.security.Principal;

import org.robbins.moviefinder.controllers.AbstractController;
import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.Filters;
import org.robbins.moviefinder.dtos.MoviesDto;
import org.robbins.moviefinder.services.ActorAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("actoralerts")
public class ActorAlertController extends AbstractController {
    final Logger logger = LoggerFactory.getLogger(ActorAlertController.class);

    private final ActorAlertService actorAlertService;

    public ActorAlertController(final ActorAlertService actorAlertService) {
        this.actorAlertService = actorAlertService;
    }

    @GetMapping
    public ActorsDto findMyActors(final Principal principal) {
        final String userEmail = extractUserEmailFromPrincipal(principal);

        final ActorsDto actorAlertsDto = actorAlertService.findAMyActors(userEmail);

        return actorAlertsDto;
    }

    @GetMapping("/actors/counts")
    public ActorCountsDto findMyActorCounts(final Principal principal) {
        final String userEmail = extractUserEmailFromPrincipal(principal);

        final ActorCountsDto actorCounts = actorAlertService.findMyActorCounts(userEmail);

        return actorCounts;
    }

    @GetMapping("/{actorId}")
    public Boolean doesActorAlertExistForUser(@PathVariable("actorId") final Long actorId, final Principal principal) {
        final String userEmail = extractUserEmailFromPrincipal(principal);

       return actorAlertService.isUserFollowingActor(userEmail, actorId);
    }

    @PostMapping
    public void createActorAlert(@RequestBody ActorDto actor, final Principal principal) {
        final String userEmail = extractUserEmailFromPrincipal(principal);

        actorAlertService.saveActorAlert(userEmail, actor.getActorId());
    }

    @DeleteMapping("/{actorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActorAlert(@PathVariable("actorId") final Long actorId, final Principal principal) {
        final String userEmail = extractUserEmailFromPrincipal(principal);

        try {
            actorAlertService.deleteActorAlert(userEmail, actorId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Alert Not found for user");
        }
        return;
    }

    @GetMapping("/movies")
    public MoviesDto findMyMovies(@RequestParam(required = false) final Filters filter, final Principal principal) {
        final String userEmail = extractUserEmailFromPrincipal(principal);

        final MoviesDto movies = actorAlertService.findMyMovies(userEmail, filter);
        return movies;
    }
}
