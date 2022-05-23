package org.robbins.moviefinder.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.enums.MovieSort;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.PersonCreditsFilteringService;
import org.robbins.moviefinder.services.PersonCreditsSortingService;
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
    private final PersonCreditsFilteringService movieFilterService;
    private final PersonCreditsSortingService personCreditsSortingService;

    public ActorServiceImpl(final PersonService personService, 
            final PersonCreditsFilteringService movieFilterService,
            final PersonCreditsSortingService personCreditsSortingService) {

        this.personService = personService;
        this.movieFilterService = movieFilterService;
        this.personCreditsSortingService = personCreditsSortingService;
    }

    @Override
    public ActorsDto findPopularActors() {
        long minMovieCountForPopularActors = 5;
        final long numberOfPopularPeopleToReturn = 12;

        final ActorsDto actors = new ActorsDto();
        final PersonResultsPage page = personService.findPopularPeople();

        page.getResults()
                .parallelStream()
                .forEach(person -> actors.getActors()
                        .add(findActorWithMovies(Long.valueOf(person.getId()), Optional.empty(),
                                Optional.empty(), Optional.empty())));

        final List<ActorDto> filteredActors = actors.getActors()
                .stream()
                .filter(actor -> actor.getMovieCredits().getCast().size() > minMovieCountForPopularActors)
                .limit(numberOfPopularPeopleToReturn)
                .collect(Collectors.toList());

        actors.setActors(filteredActors);

        actors.getActors().sort(Comparator.comparing((ActorDto actor) -> actor.getPerson().getPopularity()).reversed());

        return actors;
    }

    @Override
    public ActorsDto findActors(final List<Long> actorIds) {
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
    public ActorDto findActorWithMovies(final Long actorId, final Optional<MovieFilter> filter,
            final Optional<MovieSort> sort, final Optional<User> user) {

        final ActorDto actor = findActor(actorId);
        final PersonCredits allCredits = personService.findPersonMovieCredits(actorId);
        final PersonCredits filteredCredits = filterPersonCredits(allCredits, filter, user);
        final PersonCredits sortedCredits = sortPersonCredits(filteredCredits, sort);
        actor.setMovieCredits(sortedCredits);

        return actor;
    }

    private PersonCredits filterPersonCredits(final PersonCredits credits,
            final Optional<MovieFilter> filter, final Optional<User> user) {

        if (filter.isEmpty()) {
            return credits;
        } else {
            return movieFilterService.filter(credits, filter.get(), user);
        }
    }

    private PersonCredits sortPersonCredits(final PersonCredits credits, final Optional<MovieSort> sort) {
        if (sort.isEmpty()) {
            return credits;
        } else {
            return personCreditsSortingService.sort(credits, sort.get());
        }
    }
}
