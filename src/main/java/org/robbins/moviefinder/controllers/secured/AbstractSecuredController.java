package org.robbins.moviefinder.controllers.secured;

import java.security.Principal;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public abstract class AbstractSecuredController {
    
    String extractUserEmailFromPrincipal(final Principal principal) {
        final JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return (String) token.getTokenAttributes().get("email");
    }
}
