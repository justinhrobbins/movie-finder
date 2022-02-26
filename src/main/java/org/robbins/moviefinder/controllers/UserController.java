package org.robbins.moviefinder.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserRepository userRepository;

    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Map<String, String> getUser(Principal principal) {
        final JwtAuthenticationToken token = (JwtAuthenticationToken) principal;

        final Map<String, String> userDetails = new HashMap<>();
        userDetails.put("name", (String) token.getTokenAttributes().get("name"));
        userDetails.put("email", (String) token.getTokenAttributes().get("email"));
        
        handleLoggedInUser(userDetails);

        return userDetails;
    }

    private User handleLoggedInUser(final Map<String, String> userDetails) {
        Optional<User> existingUser = userRepository.findByEmail(userDetails.get("email"));

        if (existingUser.isEmpty()) {
            logger.debug("User Not found");
            final User newUser = new User(userDetails.get("email"), userDetails.get("email"));
            return userRepository.save(newUser);
        }
        logger.debug("User Found");
        return existingUser.get();
    }
}
