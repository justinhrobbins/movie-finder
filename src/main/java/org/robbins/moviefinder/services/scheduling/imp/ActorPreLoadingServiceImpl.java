package org.robbins.moviefinder.services.scheduling.imp;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.MyActorService;
import org.robbins.moviefinder.services.UserService;
import org.robbins.moviefinder.services.scheduling.ActorPreLoadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ActorPreLoadingServiceImpl implements ActorPreLoadingService {
    final Logger logger = LoggerFactory.getLogger(ActorPreLoadingServiceImpl.class);

    private final UserService userService;
    private final MyActorService myActorService;

    public ActorPreLoadingServiceImpl(final UserService userService, final MyActorService myActorService) {
        this.userService = userService;
        this.myActorService = myActorService;
    }

    @Scheduled(fixedDelay = 60000)
    @Override
    public void preLoadActors() {
        logger.debug("Begin preLoadActors()");

        List<User> users = userService.findAll();
        logger.debug("Preloading Actors for {} users", users.size());

        users.parallelStream()
                .forEach(user -> myActorService.findAMyActors(user, Optional.empty(), Optional.empty()));
        logger.debug("End preLoadActors()");
    }

}
