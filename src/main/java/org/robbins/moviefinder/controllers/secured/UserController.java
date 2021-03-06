package org.robbins.moviefinder.controllers.secured;

import java.security.Principal;

import org.robbins.moviefinder.controllers.AbstractController;
import org.robbins.moviefinder.dtos.UserDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController extends AbstractController {
    final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserDto getUser(final Principal principal) {
        final JwtAuthenticationToken token = (JwtAuthenticationToken) principal;

        final UserDto userDto = new UserDto();
        userDto.setEmail((String) token.getTokenAttributes().get("email"));
        userDto.setName((String) token.getTokenAttributes().get("name"));

        final UserDto loggedInUser = userService.handleLoggedInUser(userDto);
        return loggedInUser;
    }

    @PostMapping
    public UserDto saveSubscriptionServices(final Principal principal, @RequestBody UserDto userDto) {
        final User user = extractUserFromPrincipal(principal).get();
        final UserDto updatedUser = userService.updateUserSubscriptions(user, userDto.getStreamingServices());
        return updatedUser;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
