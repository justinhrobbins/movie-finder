package org.robbins.moviefinder.services.impl;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.repositories.ActorAlertRepository;
import org.robbins.moviefinder.services.ActorAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActorAlertServiceImpl implements ActorAlertService {
    final Logger logger = LoggerFactory.getLogger(ActorAlertService.class);

    private final ActorAlertRepository actorAlertRepository;

    public ActorAlertServiceImpl(final ActorAlertRepository actorAlertRepository) {
        this.actorAlertRepository = actorAlertRepository;
    }

    @Override
    public Boolean isUserFollowingActor(final User user, final Long actorId) {
        Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);

        return actorAlert.isPresent() ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public void saveActorAlert(final User user, final Long actorId) {
        actorAlertRepository.save(new ActorAlert(user, actorId));
    }

    @Override
    public void deleteActorAlert(final User user, final Long actorId) {
        final Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);

        if (!actorAlert.isPresent()) {
            throw new RuntimeException("Actor Alert not found");
        } else {
            actorAlertRepository.delete(actorAlert.get());
            return;
        }
    }

    @Override
    public List<ActorAlert> findActorAlerts(User user) {
        return actorAlertRepository.findByUser(user);
    }

}
