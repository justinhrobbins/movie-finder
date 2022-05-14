package org.robbins.moviefinder.services.impl;

import java.util.Comparator;
import java.util.List;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.dtos.Filters;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.MovieFilterinService;
import org.robbins.moviefinder.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.TmdbPeople.PersonResultsPage;
import info.movito.themoviedbapi.model.people.PersonCredits;
import info.movito.themoviedbapi.model.people.PersonPeople;

@Service
public class ActorServiceImpl implements ActorService {
    final Logger logger = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final PersonService personService;
    private final MovieFilterinService movieFilterService;

    public ActorServiceImpl(final PersonService personService, final MovieFilterinService movieFilterService) {
        this.personService = personService;
        this.movieFilterService = movieFilterService;
    }

    @Override
    public ActorsDto findPopularActors() {
        final ActorsDto actors = new ActorsDto();
        final PersonResultsPage page = personService.findPopularPeople();

        page.getResults()
                .parallelStream()
                .forEach(person -> actors.getActors().add(findActor(Long.valueOf(person.getId()))));

        actors.getActors()
                .sort(Comparator.comparing((ActorDto actor) -> actor.getPerson().getPopularity()).reversed());
        return actors;
    }

    @Override
    public ActorsDto findActors(final List<Long> actorIds, final User user) {
        final ActorsDto actors = new ActorsDto();

        actorIds
                .parallelStream()
                .forEach(id -> actors.getActors().add(findActor(id)));

        return actors;
    }

    @Override
    public ActorDto findActor(final Long actorId) {
        final ActorDto actor = new ActorDto(Long.valueOf(actorId));
        final ActorDto actorWithPerson = addPersonToActor(actor);

        return actorWithPerson;
    }

    private ActorDto addPersonToActor(final ActorDto actor) {
        final PersonPeople person = personService.findPerson(actor.getActorId());
        actor.setPerson(person);
        return actor;
    }

    @Override
    public ActorDto findActorWithMovies(final Long actorId, final Filters filter, final User user) {
        final ActorDto actor = findActor(actorId);
        final PersonCredits allCredits = personService.findPersonMovieCredits(actorId);
        final PersonCredits filteredCredits = filterCredits(allCredits, filter, user);
        actor.setMovieCredits(filteredCredits);
        return actor;
    }

    // move this method into MovieFilterService?
    private PersonCredits filterCredits(final PersonCredits credits, final Filters filter, final User user) {

        switch (filter) {
            case RECENT:
                return movieFilterService.filterByRecent(credits);
            case UPCOMING:
                return movieFilterService.filterByUpcoming(credits);
            case SUBSCRIPTIONS:
                return movieFilterService.filterBySubscriptions(credits, user);
        }
        return credits;
    }
}
