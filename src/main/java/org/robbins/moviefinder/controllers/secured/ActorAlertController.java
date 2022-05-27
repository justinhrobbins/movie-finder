package org.robbins.moviefinder.controllers.secured;

import java.security.Principal;

import org.robbins.moviefinder.controllers.AbstractController;
import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.ActorAlertService;
import org.robbins.moviefinder.services.UserService;
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
public class ActorAlertController extends AbstractController {
    final Logger logger = LoggerFactory.getLogger(ActorAlertController.class);

    private final UserService userService;
    private final ActorAlertService actorAlertService;

    public ActorAlertController(final UserService userService, final ActorAlertService actorAlertService) {
        this.userService = userService;
        this.actorAlertService = actorAlertService;
    }

    @GetMapping("/{actorId}")
    public Boolean doesActorAlertExistForUser(@PathVariable("actorId") final Long actorId, final Principal principal) {
        final User user = extractUserFromPrincipal(principal).get();

        return actorAlertService.isUserFollowingActor(user, actorId);
    }

    @PostMapping
    public void createActorAlert(@RequestBody ActorDto actor, final Principal principal) {
        final User user = extractUserFromPrincipal(principal).get();

        actorAlertService.saveActorAlert(user, actor.getActorId());
    }

    @DeleteMapping("/{actorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActorAlert(@PathVariable("actorId") final Long actorId, final Principal principal) {
        final User user = extractUserFromPrincipal(principal).get();

        try {
            actorAlertService.deleteActorAlert(user, actorId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Alert Not found for user");
        }
        return;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
