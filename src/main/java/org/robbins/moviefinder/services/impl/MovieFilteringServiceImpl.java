package org.robbins.moviefinder.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.services.FlatrateProviderService;
import org.robbins.moviefinder.services.PersonCreditsFilteringService;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;

@Service
public class MovieFilteringServiceImpl implements PersonCreditsFilteringService {

    private final FlatrateProviderService flatrateProviderService;

    public MovieFilteringServiceImpl(final FlatrateProviderService flatrateProviderService) {
        this.flatrateProviderService = flatrateProviderService;
    }

    @Override
    public PersonCredits filter(PersonCredits credits, MovieFilter filter, Optional<User> user) {

        switch (filter) {
            case ALL:
                return credits;
            case RECENT:
                return filterByRecent(credits);
            case UPCOMING:
                return filterByUpcoming(credits);
            case SUBSCRIPTIONS:
                return filterBySubscriptions(credits, user);
            default:
                return credits;
        }
    }

    @Override
    public PersonCredits filterBySubscriptions(final PersonCredits personCredits, final Optional<User> user) {
        final PersonCredits filteredPersonCredits = new PersonCredits();

        if (user.isEmpty()) {
            filteredPersonCredits.setCast(new ArrayList<>());
            return filteredPersonCredits;
        }

        final List<PersonCredit> filteredCastCredits = personCredits.getCast()
                .stream()
                .filter(credit -> flatrateProviderService.movieIsOnSubscription(credit.getId(), user.get()))
                .collect(Collectors.toList());

        filteredPersonCredits.setCast(filteredCastCredits);
        return filteredPersonCredits;
    }

    @Override
    public PersonCredits filterByUpcoming(final PersonCredits personCredits) {
        final LocalDate today = LocalDate.now();

        final List<PersonCredit> filteredCastCredits = personCredits.getCast()
                .stream()
                .filter(credit -> !(credit.getReleaseDate() == null))
                .filter(credit -> credit.getReleaseDate().length() > 0)
                .filter(credit -> LocalDate.parse(credit.getReleaseDate()).isAfter(today))
                .collect(Collectors.toList());

        final PersonCredits filteredPersonCredits = new PersonCredits();
        filteredPersonCredits.setCast(filteredCastCredits);
        return filteredPersonCredits;
    }

    @Override
    public PersonCredits filterByRecent(final PersonCredits personCredits) {
        final List<PersonCredit> filteredCastCredits = personCredits.getCast()
                .stream()
                .filter(credit -> !(credit.getReleaseDate() == null))
                .filter(credit -> credit.getReleaseDate().length() > 0)
                .filter(credit -> isWithinRecentRange(LocalDate.parse(credit.getReleaseDate())))
                .collect(Collectors.toList());

        final PersonCredits filteredPersonCredits = new PersonCredits();
        filteredPersonCredits.setCast(filteredCastCredits);
        return filteredPersonCredits;
    }

    private boolean isWithinRecentRange(LocalDate date) {
        final LocalDate startOfRange = LocalDate.now().minusMonths(12);
        final LocalDate endOfRange = LocalDate.now();

        return !(date.isBefore(startOfRange) || date.isAfter(endOfRange));
    }
}
