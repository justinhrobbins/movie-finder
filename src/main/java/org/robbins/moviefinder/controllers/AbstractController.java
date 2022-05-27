package org.robbins.moviefinder.controllers;

import java.security.Principal;
import java.util.Optional;

import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.UserService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public abstract class AbstractController {

    public Optional<User> extractUserFromPrincipal(final Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        
        final JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        final String emailAddress = (String) token.getTokenAttributes().get("email");
        final Optional<User> user = getUserService().findByEmailUser(emailAddress);
        return user;
    }

    public abstract UserService getUserService();
}
