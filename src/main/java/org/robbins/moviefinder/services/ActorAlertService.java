package org.robbins.moviefinder.services;

import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;

public interface ActorAlertService {
    public void saveActorAlert(final String userEmail, final Long actorId);
    public void deleteActorAlert(final String userEmail, final Long actorId);
    public Optional<ActorDto> findByUserAndActorId(final String userEmail, final Long actorId);
    public ActorsDto findActorAlertsForUser(final String userEmail);
}
