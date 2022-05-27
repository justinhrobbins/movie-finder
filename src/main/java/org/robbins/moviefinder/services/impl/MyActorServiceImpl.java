package org.robbins.moviefinder.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.robbins.moviefinder.dtos.ActorCountsDto;
import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.ActorSort;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.services.ActorAlertService;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.MyActorService;
import org.robbins.moviefinder.services.counting.ActorMovieCountService;
import org.robbins.moviefinder.services.counting.ActorsCountService;
import org.robbins.moviefinder.services.filtering.ActorFilteringService;
import org.robbins.moviefinder.services.sorting.ActorSortingService;
import org.springframework.stereotype.Service;

@Service
public class MyActorServiceImpl implements MyActorService {

    private final ActorAlertService actorAlertService;
    private final ActorService actorService;
    private final ActorsCountService actorCountService;
    private final ActorMovieCountService movieCountService;
    private final ActorFilteringService actorFilteringService;
    private final ActorSortingService actorSortingService;

    public MyActorServiceImpl(final ActorAlertService actorAlertService,
            final ActorService actorService,
            final ActorsCountService actorCountService,
            final ActorMovieCountService movieCountService,
            final ActorFilteringService actorFilteringService,
            final ActorSortingService actorSortingService) {

        this.actorAlertService = actorAlertService;
        this.actorService = actorService;
        this.actorCountService = actorCountService;
        this.movieCountService = movieCountService;
        this.actorFilteringService = actorFilteringService;
        this.actorSortingService = actorSortingService;
    }

    @Override
    public ActorsDto findAMyActors(final User user, final Optional<MovieFilter> filter,
            final Optional<ActorSort> sort) {

        final List<ActorAlert> followedActors = actorAlertService.findActorAlerts(user);

        final ActorsDto myActors = findMyActorsWithCounts(followedActors, user);
        myActors.setActorCounts(actorCountService.calculateTotals(myActors));
        myActors.setMovieCounts(movieCountService.calculateMovieCounts(myActors));

        final List<ActorDto> filteredActors = filterActors(myActors.getActors(), filter);
        final List<ActorDto> sortedActors = sortActors(filteredActors, sort);
        myActors.setActors(sortedActors);

        return myActors;
    }

    @Override
    public ActorCountsDto findMyActorCounts(final User user) {
        final List<ActorAlert> followedActors = actorAlertService.findActorAlerts(user);
        final ActorsDto actorsWithMovieCounts = findMyActorsWithCounts(followedActors, user);
        final ActorCountsDto actorCounts = actorCountService.calculateTotals(actorsWithMovieCounts);
        return actorCounts;
    }

    private ActorsDto findMyActorsWithCounts(final List<ActorAlert> followedActors, final User user) {
        final ActorsDto actors = findMyActorsWithoutCounts(followedActors);
        final ActorsDto actorsWithMovieCounts = populateActorMovieCounts(actors, user);
        return actorsWithMovieCounts;
    }

    private ActorsDto findMyActorsWithoutCounts(final List<ActorAlert> followedActors) {
        List<Long> actorIds = followedActors
                .stream()
                .map(ActorAlert::getActorId)
                .collect(Collectors.toList());
        return actorService.findActors(actorIds);
    }

    private List<ActorDto> filterActors(final List<ActorDto> actors, final Optional<MovieFilter> filter) {
        if (filter.isEmpty()) {
            return actors;
        }
        return actorFilteringService.filter(actors, filter.get());
    }

    private List<ActorDto> sortActors(final List<ActorDto> actors, final Optional<ActorSort> sort) {
        if (sort.isEmpty()) {
            return actors;
        }
        return actorSortingService.sort(actors, sort.get());
    }

    private ActorsDto populateActorMovieCounts(final ActorsDto actors, final User user) {
        actors.getActors()
                .forEach(actor -> {
                    actor.setMovieCounts(
                            movieCountService.findActorMovieCounts(actor.getActorId(), Optional.of(user)));
                });

        return actors;
    }
}
