package org.robbins.moviefinder.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.FlatrateProviderService;
import org.robbins.moviefinder.services.MovieService;
import org.robbins.moviefinder.services.UserService;
import org.springframework.stereotype.Service;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.WatchProviders;
import info.movito.themoviedbapi.model.WatchProviders.Provider;
import info.movito.themoviedbapi.model.WatchProviders.Results.US;

@Service
public class FlatrateProviderServiceImpl implements FlatrateProviderService {
    
    private final MovieService movieService;
    private final UserService userService;

    public FlatrateProviderServiceImpl(final MovieService movieService, final UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @Override
    public List<Provider> findFlatrateProviders(final int movieId) {
        final List<Provider> flatRateProviders = new ArrayList<>();

        final MovieDb movie = movieService.findMovieWatchProviders(movieId, "en");
        final WatchProviders watchProviders = movie.getWatchProviders();

        if (watchProviders == null) {
            return flatRateProviders;
        }

        final WatchProviders.Results results = watchProviders.getResults();
        final US us = results.getUS();

        if ((us != null) && (us.getFlatrateProviders() != null)) {
            flatRateProviders.addAll(us.getFlatrateProviders());
        }

        return flatRateProviders;
    }

    @Override
    public boolean movieIsOnSubscription(final int movieId, final User user) {
        final List<String> userSubscriptions = userService.convertStreamingServices(user);
        final List<Provider> flatrateProviders = findFlatrateProviders(movieId);

        Optional<Provider> movieSubscriberMatch = flatrateProviders.stream()
                .filter(provider -> providerMatchesSubscription(userSubscriptions, provider))
                .findFirst();

        return movieSubscriberMatch.isPresent() ? true : false;
    }

    private boolean providerMatchesSubscription(final List<String> userSubscriptions, final Provider provider) {
        return userSubscriptions.stream()
                .anyMatch(subscription -> StringUtils.equalsIgnoreCase(subscription, provider.getProviderName()));
    }
}
