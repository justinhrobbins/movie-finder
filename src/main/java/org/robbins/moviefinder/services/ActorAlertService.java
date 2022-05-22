package org.robbins.moviefinder.services;

import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorAlertsDto;
import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.Filters;
import org.robbins.moviefinder.dtos.MoviesDto;

public interface ActorAlertService {
    public void saveActorAlert(final String userEmail, final Long actorId);
    public void deleteActorAlert(final String userEmail, final Long actorId);
    public ActorAlertsDto findMyActorAlerts(final String userEmail, final Filters filter);
    public ActorCountsDto findMyActorCounts(final String userEmail);
    public Boolean isUserFollowingActor(final String userEmail, final Long actorId);
    public ActorsDto findAMyActors(final String userEmail);
    public MoviesDto findMyMovies(final String userEmail, final Optional<Filters> filter);
}
