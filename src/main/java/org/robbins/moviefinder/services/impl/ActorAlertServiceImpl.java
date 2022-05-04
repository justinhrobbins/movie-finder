package org.robbins.moviefinder.services.impl;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.ActorDto;
import org.robbins.moviefinder.dtos.ActorsDto;
import org.robbins.moviefinder.entities.ActorAlert;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.repositories.ActorAlertRepository;
import org.robbins.moviefinder.services.ActorAlertService;
import org.robbins.moviefinder.services.ActorService;
import org.robbins.moviefinder.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class ActorAlertServiceImpl implements ActorAlertService {

    private final UserService userService;
    private final ActorService actorService;
    private final ActorAlertRepository actorAlertRepository;

    public ActorAlertServiceImpl(final UserService userService, final ActorAlertRepository actorAlertRepository,
            final ActorService actorService) {
        this.userService = userService;
        this.actorAlertRepository = actorAlertRepository;
        this.actorService = actorService;
    }

    @Override
    public Optional<ActorDto> findByUserAndActorId(String userEmail, Long actorId) {
        final User user = userService.findByEmailUser(userEmail).get();

        Optional<ActorAlert> actorAlert = actorAlertRepository.findByUserAndActorId(user, actorId);

        if (!actorAlert.isPresent()) {
            return Optional.empty();
        } else {
            final ActorDto actor = new ActorDto(actorAlert.get().getActorId());
            return Optional.of(actor);
        }
    }

    @Override
    public void saveActorAlert(String userEmail, Long actorId) {
        final User user = userService.findByEmailUser(userEmail).get();
        actorAlertRepository.save(new ActorAlert(user, actorId));
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

    @Override
    public ActorsDto findActorAlertsForUser(final String userEmail) {
        final User user = userService.findByEmailUser(userEmail).get();

        List<ActorAlert> actorAlerts = actorAlertRepository.findByUser(user);
        final ActorsDto actorAlertsDto = convertActorAlerts(actorAlerts);

        return actorAlertsDto;
    }

    private ActorsDto convertActorAlerts(final List<ActorAlert> actorAlerts) {
        final ActorsDto actorAlertsDto = new ActorsDto();

        actorAlerts
                .parallelStream()
                .forEach(actorAlert -> {
                    final ActorDto actor = actorService.findByActorId(actorAlert.getActorId());
                    actorAlertsDto.getActors().add(actor);
                });

        final ActorsDto actorAlertsDtoWithTotals = calculateTotals(actorAlertsDto);
        return actorAlertsDtoWithTotals;
    }

    private ActorsDto calculateTotals(final ActorsDto actorAlertsDto) {
        actorAlertsDto.setActorCount(calculateActorAlertsCount(actorAlertsDto));
        actorAlertsDto.setUpcomingMovieCount(calculateUpcomingMovieCount(actorAlertsDto));
        actorAlertsDto.setRecentMovieCount(calculateRecentMovieCount(actorAlertsDto));

        return actorAlertsDto;
    }

    private int calculateActorAlertsCount(final ActorsDto actorAlertsDto) {
        return actorAlertsDto.getActors().size();
    }

    private int calculateUpcomingMovieCount(final ActorsDto actorAlertsDto) {
        long upcomingMovieCount = actorAlertsDto.getActors()
                .parallelStream()
                .filter(actorAlert -> actorAlert.getMovieCounts().getUpcomingMovies() > 0)
                .count();

        return Math.toIntExact(upcomingMovieCount);
    }

    private int calculateRecentMovieCount(final ActorsDto actorAlertsDto) {
        long recentMovieCount = actorAlertsDto.getActors()
                .parallelStream()
                .filter(actorAlert -> actorAlert.getMovieCounts().getRecentMovies() > 0)
                .count();

        return Math.toIntExact(recentMovieCount);
    }
}
