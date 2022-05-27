package org.robbins.moviefinder.services;

import java.util.List;

import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;

public interface ActorAlertService {
    public void saveActorAlert(final User user, final Long actorId);

    public void deleteActorAlert(final User user, final Long actorId);

    public Boolean isUserFollowingActor(final User user, final Long actorId);

    public List<ActorAlert> findActorAlerts(final User user);
}
