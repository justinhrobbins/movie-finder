package org.robbins.moviefinder.controllers;

import java.security.Principal;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public abstract class AbstractController {
    
    public String extractUserEmailFromPrincipal(final Principal principal) {
        final JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return (String) token.getTokenAttributes().get("email");
    }
}
