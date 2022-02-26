package org.robbins.moviefinder.repositories;

import java.util.Optional;

import org.robbins.moviefinder.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

   Optional<User> findByEmail(final String email);
}
