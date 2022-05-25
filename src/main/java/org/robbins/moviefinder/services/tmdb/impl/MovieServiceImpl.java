package org.robbins.moviefinder.services.tmdb.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.robbins.moviefinder.dtos.ActorCastDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.ActorAlertService;
import org.robbins.moviefinder.services.tmdb.MovieService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.ResponseStatusException;
import info.movito.themoviedbapi.model.people.PersonCast;

@Service
public class MovieServiceImpl implements MovieService {
    final TmdbApi tmdbApi;
    final ActorAlertService actorAlertService;

    public MovieServiceImpl(final TmdbApi tmdbApi, final ActorAlertService actorAlertService) {
        this.tmdbApi = tmdbApi;
        this.actorAlertService = actorAlertService;
    }

    @Override
    @Cacheable("moviewatchproviders")
    public MovieDb findMovieWatchProviders(int movieId, String language) {
        try {
            return tmdbApi.getMovies().getMovie(movieId, language, MovieMethod.watch_providers);
        } catch (ResponseStatusException ex) {
            return new MovieDb();
        }
    }

    @Override
    @Cacheable("moviecast")
    public List<ActorCastDto> findMovieCast(int movieId, final Optional<User> user) {
        try {
            List<PersonCast> personCast = tmdbApi.getMovies().getCredits(movieId).getCast();
            List<ActorCastDto> actors = personCast
                    .stream()
                    .map(person -> convert(person, user))
                    .collect(Collectors.toList());

            List<ActorCastDto> sortedActors = sortCast(actors);
            return sortedActors;

        } catch (ResponseStatusException ex) {
            return new ArrayList<>();
        }
    }

    private ActorCastDto convert(final PersonCast person, final Optional<User> user) {
        final ActorCastDto actor = new ActorCastDto();
        actor.setActorId(Long.valueOf(person.getId()));
        actor.setPerson(person);
        if (user.isPresent()) {
            actor.setFollowedByUser(
                    actorAlertService.isUserFollowingActor(user.get(), Long.valueOf(person.getId())));
        }
        return actor;
    }

    private List<ActorCastDto> sortCast(final List<ActorCastDto> actors) {
        final List<ActorCastDto> sortedActors = new ArrayList<>();

        final List<ActorCastDto> followedActors = actors.stream()
                .filter(actor -> actor.isFollowedByUser())
                .collect(Collectors.toList());

        final List<ActorCastDto> unfollowedActors = actors.stream()
                .filter(actor -> !actor.isFollowedByUser())
                .collect(Collectors.toList());

        followedActors.sort(Comparator.comparing(actor -> actor.getPerson().getOrder()));
        unfollowedActors.sort(Comparator.comparing(actor -> actor.getPerson().getOrder()));

        sortedActors.addAll(followedActors);
        sortedActors.addAll(unfollowedActors);

        return sortedActors;
    }
}
