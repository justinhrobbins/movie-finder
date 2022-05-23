package org.robbins.moviefinder.services;

import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.Filters;
import org.robbins.moviefinder.dtos.MoviesDto;

public interface ActorAlertService {
    public void saveActorAlert(final String userEmail, final Long actorId);
    public void deleteActorAlert(final String userEmail, final Long actorId);
    public ActorCountsDto findMyActorCounts(final String userEmail);
    public Boolean isUserFollowingActor(final String userEmail, final Long actorId);
    public ActorsDto findAMyActors(final String userEmail, final Optional<Filters> filter);
    public MoviesDto findMyMovies(final String userEmail, final Optional<Filters> filter);
}
