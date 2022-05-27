package org.robbins.moviefinder.controllers.secured;

import java.security.Principal;
import java.util.Optional;

import org.robbins.moviefinder.controllers.AbstractController;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.ActorSort;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.services.MyActorService;
import org.robbins.moviefinder.services.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("myactors")
public class MyActorsController extends AbstractController {

    private final UserService userService;
    private final MyActorService myActorService;

    public MyActorsController(final UserService userService, final MyActorService myActorService) {
        this.userService = userService;
        this.myActorService = myActorService;
    }

    @GetMapping
    public ActorsDto findMyActors(@RequestParam(name = "filter", required = false) final MovieFilter filter,
            @RequestParam(name = "sort", required = false) final ActorSort sort,
            final Principal principal) {

        final User user = extractUserFromPrincipal(principal).get();
        Optional<MovieFilter> optionalFilter = filter != null ? Optional.of(filter) : Optional.empty();
        Optional<ActorSort> optionalSort = sort != null ? Optional.of(sort) : Optional.empty();

        final ActorsDto actorAlertsDto = myActorService.findAMyActors(user, optionalFilter, optionalSort);

        return actorAlertsDto;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
