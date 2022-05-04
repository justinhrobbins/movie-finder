package org.robbins.moviefinder.controllers.secured;

import java.security.Principal;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("actoralerts")
public class ActorAlertController extends AbstractSecuredController {
    Logger logger = LoggerFactory.getLogger(ActorAlertController.class);

    private final ActorAlertService actorAlertService;

    public ActorAlertController(final ActorAlertService actorAlertService) {
        this.actorAlertService = actorAlertService;
    }

    @GetMapping
    public ActorsDto findActorAlerts(final Principal principal) {
        final String userEmail = extractUserEmailFromPrincipal(principal);
        
        final ActorsDto actorAlertsDto = actorAlertService.findActorAlertsForUser(userEmail);
        
        return actorAlertsDto;
    }

    @GetMapping("/{actorId}")
    public Boolean doesActorAlertExistForUser(@PathVariable("actorId") final Long actorId, final Principal principal) {
        final String userEmail = extractUserEmailFromPrincipal(principal);

        Optional<ActorDto> actor = actorAlertService.findByUserAndActorId(userEmail, actorId);

        if (actor.isPresent()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
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
}
