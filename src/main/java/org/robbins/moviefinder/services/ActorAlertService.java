package org.robbins.moviefinder.services;

import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorAlertDto;
import org.robbins.moviefinder.dtos.ActorAlertsDto;

public interface ActorAlertService {
    public ActorAlertsDto findActorAlertsForUser(final String userEmail);
    public Optional<ActorAlertDto> findByUserAndActorId(final String userEmail, final Long actorId);
    public ActorAlertDto saveActorAlert(final String userEmail, final Long actorId);
    public void deleteActorAlert(final String userEmail, final Long actorId);
}
