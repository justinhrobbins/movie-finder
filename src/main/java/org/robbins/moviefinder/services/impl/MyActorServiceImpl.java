package org.robbins.moviefinder.services.impl;

import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.enums.ActorSort;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.services.MyActorService;
import org.springframework.stereotype.Service;

@Service
public class MyActorServiceImpl implements MyActorService {

    @Override
    public ActorsDto findAMyActors(final String userEmail, final Optional<MovieFilter> filter,
            final Optional<ActorSort> sort) {

        return null;
    }
}
