package org.robbins.moviefinder.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorAlertDto;
import org.robbins.moviefinder.dtos.ActorAlertsDto;
import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.repositories.ActorAlertRepository;
import org.robbins.moviefinder.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
public class ActorAlertController {
    Logger logger = LoggerFactory.getLogger(ActorAlertController.class);
    
    private final UserRepository userRepository;
    private final ActorAlertRepository actorAlertRepository;

    public ActorAlertController(final UserRepository userRepository, final ActorAlertRepository actorAlertRepository) {
        this.userRepository = userRepository;
        this.actorAlertRepository = actorAlertRepository;
    }

    @GetMapping
    public ActorAlertsDto findActorAlerts(final Principal principal) {
        final User user = findExistingUser(principal);
        
        List<ActorAlert> actorAlerts = actorAlertRepository.findByUser(user);
        final ActorAlertsDto actorAlertsDto = convertActorAlerts(actorAlerts);
        
        return actorAlertsDto;
    }

    @GetMapping("/{actorId}")
    public ActorAlertDto findActorAlert(@PathVariable("actorId") final Long actorId, final Principal principal) {
        final User user = findExistingUser(principal);

        Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);
        
        if (!actorAlert.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Alert Not found for user");
        } else {
            final ActorAlertDto actorAlertDto = new ActorAlertDto(actorAlert.get().getActorId());
            return actorAlertDto;
        }
    }

    private ActorAlertsDto convertActorAlerts(final List<ActorAlert> actorAlerts) {
        final ActorAlertsDto actorAlertsDto = new ActorAlertsDto();
        actorAlerts.forEach(actorAlert -> {
            final ActorAlertDto actorAlertDto = new ActorAlertDto(actorAlert.getActorId());
            actorAlertsDto.getActorAlerts().add(actorAlertDto);
        });

        return actorAlertsDto;
    }

    @PostMapping
    public void createActorAlert(@RequestBody ActorAlertDto actorAlertDto, final Principal principal) {
        final User user = findExistingUser(principal);

        final ActorAlert actorAlert = new ActorAlert(user, actorAlertDto.getActorId());
        
        actorAlertRepository.save(actorAlert);
    }

    @DeleteMapping("/{actorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActorAlert(@PathVariable("actorId") final Long actorId, final Principal principal) {
        final User user = findExistingUser(principal);

        final Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);

        if (!actorAlert.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Alert Not found for user");
        } else {
            actorAlertRepository.delete(actorAlert.get());
        }
    }

    private User findExistingUser(Principal principal) {
        final JwtAuthenticationToken token = (JwtAuthenticationToken) principal;

        final Map<String, String> userDetails = new HashMap<>();
        userDetails.put("name", (String) token.getTokenAttributes().get("name"));
        userDetails.put("email", (String) token.getTokenAttributes().get("email"));

        Optional<User> existingUser = userRepository.findByEmail(userDetails.get("email"));

        return existingUser.get();
    }
}
