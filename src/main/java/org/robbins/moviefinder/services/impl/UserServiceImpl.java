package org.robbins.moviefinder.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.robbins.moviefinder.dtos.UserDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.repositories.UserRepository;
import org.robbins.moviefinder.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByEmailUser(String userEmail) {
        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        return existingUser;
    }

    @Override
    public UserDto updateUserSubscriptions(final String userEmail, final List<String> streamingServices) {
        final User user = userRepository.findByEmail(userEmail).get();
        user.setStreamingServices(String.join(",", streamingServices));
        final User updatedUser = userRepository.save(user);

        return convert(updatedUser);
    }

    private UserDto convert(final User user) {
        final UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setStreamingServices(convertStreamingServices(user));

        return userDto;
    }

    @Override
    public List<String> convertStreamingServices(final User user) {
        if (user.getStreamingServices() != null) {
            return Stream.of(user.getStreamingServices().split(","))
            .map(String::trim)
            .collect(Collectors.toList());
        } else {
            return  new ArrayList<>();
        }
    }

    @Override
    public UserDto handleLoggedInUser(final UserDto userDto) {
        Optional<User> existingUser = findByEmailUser(userDto.getEmail());

        if (existingUser.isEmpty()) {
            final User newUser = new User(userDto.getEmail(), userDto.getName());
            return convert(userRepository.save(newUser));
        }
        return convert(existingUser.get());
    }
}
