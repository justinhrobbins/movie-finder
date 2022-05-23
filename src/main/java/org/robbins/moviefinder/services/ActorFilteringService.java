package org.robbins.moviefinder.services;

import java.util.List;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.enums.MovieFilter;

public interface ActorFilteringService {
    public List<ActorDto> filter(final List<ActorDto> actors, final MovieFilter filter);
}
