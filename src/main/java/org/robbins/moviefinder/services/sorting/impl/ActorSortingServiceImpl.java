package org.robbins.moviefinder.services.sorting.impl;

import java.util.Comparator;
import java.util.List;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.enums.ActorSort;
import org.robbins.moviefinder.services.sorting.ActorSortingService;
import org.springframework.stereotype.Service;

@Service
public class ActorSortingServiceImpl implements ActorSortingService {

    @Override
    public List<ActorDto> sort(List<ActorDto> actors, ActorSort sort) {
        switch (sort) {
            case POPULARITY:
                return sortByPopularity(actors);
            case NAME:
                return sortByName(actors);
            case UPCOMING:
                return sortByUpcoming(actors);
            case RECENT:
                return sortByRecent(actors);
            case SUBSCRIPTION:
                return sortBySubscriptions(actors);
            case TOTAL:
                return sortByTotal(actors);
            default:
                return actors;
        }
    }

    private List<ActorDto> sortByPopularity(List<ActorDto> actors) {
        actors.sort(Comparator.comparing((ActorDto actor) -> actor.getPerson().getPopularity()).reversed());
        return actors;
    }

    private List<ActorDto> sortByName(List<ActorDto> actors) {
        actors.sort(Comparator.comparing((ActorDto actor) -> actor.getPerson().getName()));
        return actors;
    }

    private List<ActorDto> sortByUpcoming(List<ActorDto> actors) {
        actors.sort(Comparator.comparing((ActorDto actor) -> actor.getMovieCounts().getUpcomingMovies()).reversed());
        return actors;
    }

    private List<ActorDto> sortByRecent(List<ActorDto> actors) {
        actors.sort(Comparator.comparing((ActorDto actor) -> actor.getMovieCounts().getRecentMovies()).reversed());
        return actors;
    }

    private List<ActorDto> sortBySubscriptions(List<ActorDto> actors) {
        actors.sort(Comparator.comparing((ActorDto actor) -> actor.getMovieCounts().getMoviesOnSubscriptions()).reversed());
        return actors;
    }

    private List<ActorDto> sortByTotal(List<ActorDto> actors) {
        actors.sort(Comparator.comparing((ActorDto actor) -> actor.getMovieCounts().getTotalMovies()).reversed());
        return actors;
    }

}
