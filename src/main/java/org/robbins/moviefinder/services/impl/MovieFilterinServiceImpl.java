package org.robbins.moviefinder.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.FlatrateProviderService;
import org.robbins.moviefinder.services.MovieFilterinService;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;

@Service
public class MovieFilterinServiceImpl implements MovieFilterinService {

    private final FlatrateProviderService flatrateProviderService;

    public MovieFilterinServiceImpl(final FlatrateProviderService flatrateProviderService) {
        this.flatrateProviderService = flatrateProviderService;
    }

    @Override
    public PersonCredits filterBySubscriptions(final PersonCredits personCredits, final User user) {
        final List<PersonCredit> filteredCastCredits = personCredits.getCast()
                .stream()
                .filter(credit -> flatrateProviderService.movieIsOnSubscription(credit.getId(), user))
                .collect(Collectors.toList());

        final PersonCredits filteredPersonCredits = new PersonCredits();
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
