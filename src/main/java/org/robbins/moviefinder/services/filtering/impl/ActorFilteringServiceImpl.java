package org.robbins.moviefinder.services.filtering.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.services.filtering.ActorFilteringService;
import org.springframework.stereotype.Service;

@Service
public class ActorFilteringServiceImpl implements ActorFilteringService {

    @Override
    public List<ActorDto> filter(List<ActorDto> actors, MovieFilter filter) {

        switch (filter) {
            case ALL:
                return actors;
            case RECENT:
                return filterByHasRecent(actors);
            case UPCOMING:
                return filterByHasUpcoming(actors);
            case SUBSCRIPTIONS:
                return filterByHasSubscriptions(actors);
            default: return actors;
        }
    }

    private List<ActorDto> filterByHasRecent(List<ActorDto> actors) {
        return actors
                .stream()
                .filter(actor -> actor.getMovieCounts().getRecentMovies() > 0)
                .collect(Collectors.toList());
    }

    private List<ActorDto> filterByHasUpcoming(List<ActorDto> actors) {
        return actors
                .stream()
                .filter(actor -> actor.getMovieCounts().getUpcomingMovies() > 0)
                .collect(Collectors.toList());
    }

    private List<ActorDto> filterByHasSubscriptions(List<ActorDto> actors) {
        return actors
                .stream()
                .filter(actor -> actor.getMovieCounts().getMoviesOnSubscriptions() > 0)
                .collect(Collectors.toList());
    }
}
