package org.robbins.moviefinder.services.impl;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorAlertDto;
import org.robbins.moviefinder.dtos.ActorAlertsDto;
import org.robbins.moviefinder.dtos.ActorDetailsDto;
import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.repositories.ActorAlertRepository;
import org.robbins.moviefinder.services.ActorAlertService;
import org.robbins.moviefinder.services.PersonService;
import org.robbins.moviefinder.services.UserService;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.people.Person;

@Service
public class ActorAlertServiceImpl implements ActorAlertService {

    private final UserService userService;
    private final ActorAlertRepository actorAlertRepository;
    private final PersonService personService;

    public ActorAlertServiceImpl(final UserService userService, final ActorAlertRepository actorAlertRepository,
            final PersonService personService) {
        this.userService = userService;
        this.actorAlertRepository = actorAlertRepository;
        this.personService = personService;
    }

    @Override
    public ActorAlertsDto findActorAlertsForUser(final String userEmail) {
        final User user = userService.findByEmailUser(userEmail).get();

        List<ActorAlert> actorAlerts = actorAlertRepository.findByUser(user);
        final ActorAlertsDto actorAlertsDto = convertActorAlerts(actorAlerts);

        return actorAlertsDto;
    }

    @Override
    public Optional<ActorAlertDto> findByUserAndActorId(String userEmail, Long actorId) {
        final User user = userService.findByEmailUser(userEmail).get();

        Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);

        if (!actorAlert.isPresent()) {
            return Optional.empty();
        } else {
            final ActorAlertDto actorAlertDto = new ActorAlertDto(actorAlert.get().getActorId());
            return Optional.of(actorAlertDto);
        }
    }

    @Override
    public ActorAlertDto saveActorAlert(String userEmail, Long actorId) {
        final User user = userService.findByEmailUser(userEmail).get();
        final ActorAlert actorAlert = actorAlertRepository.save(new ActorAlert(user, actorId));
        return new ActorAlertDto(actorAlert.getActorId());
    }

    @Override
    public void deleteActorAlert(final String userEmail, final Long actorId) {
        final User user = userService.findByEmailUser(userEmail).get();

        final Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);

        if (!actorAlert.isPresent()) {
            throw new RuntimeException("Actor Alert not found");
        } else {
            actorAlertRepository.delete(actorAlert.get());
            return;
        }
    }

    private ActorAlertsDto convertActorAlerts(final List<ActorAlert> actorAlerts) {
        final ActorAlertsDto actorAlertsDto = new ActorAlertsDto();

        actorAlerts.parallelStream().forEach(actorAlert -> {
            final ActorAlertDto actorAlertDto = new ActorAlertDto(actorAlert.getActorId());
            final ActorAlertDto actorWithPersonDetails = addPersonToActorAlert(actorAlertDto);
            final ActorAlertDto actorWithMovieCounts = addMovieCounts(actorWithPersonDetails);
            actorAlertsDto.getActorAlerts().add(actorWithMovieCounts);
        });

        final ActorAlertsDto actorAlertsDtoWithTotals = calculateTotals(actorAlertsDto);
        return actorAlertsDtoWithTotals;
    }

    private ActorAlertsDto calculateTotals(final ActorAlertsDto actorAlertsDto) {
        actorAlertsDto.setActorAlertCount(calculateActorAlertsCount(actorAlertsDto));
        actorAlertsDto.setUpcomingMovieCount(calculateUpcomingMovieCount(actorAlertsDto));
        actorAlertsDto.setRecentMovieCount(calculateRecentMovieCount(actorAlertsDto));

        return actorAlertsDto;
    }

    private int calculateActorAlertsCount(final ActorAlertsDto actorAlertsDto) {
        return actorAlertsDto.getActorAlerts().size();
    }

    private int calculateUpcomingMovieCount(final ActorAlertsDto actorAlertsDto) {
        long upcomingMovieCount = actorAlertsDto.getActorAlerts()
                .parallelStream()
                .filter(actorAlert -> actorAlert.getDetails().getUpcomingMovies() > 0)
                .count();

        return Math.toIntExact(upcomingMovieCount);
    }

    private int calculateRecentMovieCount(final ActorAlertsDto actorAlertsDto) {
        long recentMovieCount = actorAlertsDto.getActorAlerts()
                .parallelStream()
                .filter(actorAlert -> actorAlert.getDetails().getRecentMovies() > 0)
                .count();

        return Math.toIntExact(recentMovieCount);
    }

    private ActorAlertDto addPersonToActorAlert(final ActorAlertDto actorAlertDto) {
        final Person person = personService.findPersonDetails(Math.toIntExact(actorAlertDto.getActorId()));
        actorAlertDto.setPerson(person);
        return actorAlertDto;
    }

    private ActorAlertDto addMovieCounts(final ActorAlertDto actorAlertDto) {
        final ActorDetailsDto actorDetailsDto = personService
                .findActorDetails(Math.toIntExact(actorAlertDto.getActorId()));
        actorAlertDto.setDetails(actorDetailsDto);
        return actorAlertDto;
    }
}
