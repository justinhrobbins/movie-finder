package org.robbins.moviefinder.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {
    
    @GetMapping
    public Map<String, String> getUser(Principal principal) {
        final JwtAuthenticationToken token = (JwtAuthenticationToken) principal;

        final Map<String, String> userDetails = new HashMap<>();
        userDetails.put("name", (String) token.getTokenAttributes().get("name"));
        userDetails.put("email", (String) token.getTokenAttributes().get("email"));
        
        return userDetails;
    }
}
