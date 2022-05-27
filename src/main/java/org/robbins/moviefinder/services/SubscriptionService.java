package org.robbins.moviefinder.services;

import java.util.List;

import org.robbins.moviefinder.entities.User;

import info.movito.themoviedbapi.model.WatchProviders.Provider;

public interface SubscriptionService {
    public List<Provider> findFlatrateProviders(final int movieId);
    public boolean movieIsOnSubscription(final int movieId, final User user);
}
