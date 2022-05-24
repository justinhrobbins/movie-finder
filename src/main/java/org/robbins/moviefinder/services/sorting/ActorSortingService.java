package org.robbins.moviefinder.services.sorting;

import java.util.List;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.enums.ActorSort;

public interface ActorSortingService {
    public List<ActorDto> sort(final List<ActorDto> actors, final ActorSort sort);
}
