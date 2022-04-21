package org.robbins.moviefinder.repositories;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface ActorAlertRepository extends CrudRepository<ActorAlert, Long> {
    List<ActorAlert> findByUser(final User user);
    Optional<ActorAlert> findByUserAndActorId(final User user, final Long actorId);
}
