package org.robbins.moviefinder.services;

import java.util.List;
import java.util.Optional;

import org.robbins.moviefinder.dtos.UserDto;
import org.robbins.moviefinder.entities.User;

public interface UserService {
    public Optional<User> findByEmailUser(final String userEmail);
    public UserDto updateUserSubscriptions(final String userEmail, final List<String> streamingServices);
    public UserDto handleLoggedInUser(final UserDto userDto);
    public List<String> convertStreamingServices(final User user);
}
