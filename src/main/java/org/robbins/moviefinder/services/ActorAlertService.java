package org.robbins.moviefinder.services;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.MoviesDto;
import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.ActorSort;
import org.robbins.moviefinder.enums.MovieFilter;

public interface ActorAlertService {
    public void saveActorAlert(final User user, final Long actorId);

    public void deleteActorAlert(final User user, final Long actorId);

    public Boolean isUserFollowingActor(final User user, final Long actorId);

    public List<ActorAlert> findActorAlerts(final User user);

    public ActorsDto findAMyActors(final User user, final Optional<MovieFilter> filter,
    final Optional<ActorSort> sort);

    public ActorCountsDto findMyActorCounts(final User user);

    public MoviesDto findMyMovies(final User user, final Optional<MovieFilter> filter);
}
