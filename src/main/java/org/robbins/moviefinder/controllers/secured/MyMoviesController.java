package org.robbins.moviefinder.controllers.secured;

import java.security.Principal;
import java.util.Optional;

import org.robbins.moviefinder.controllers.AbstractController;
import org.robbins.moviefinder.dtos.MoviesDto;
import org.robbins.moviefinder.entities.User;
import org.robbins.moviefinder.enums.MovieFilter;
import org.robbins.moviefinder.services.MyMovieService;
import org.robbins.moviefinder.services.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("mymovies")
public class MyMoviesController extends AbstractController {

    private final UserService userService;
    private final MyMovieService myMovieService;

    public MyMoviesController(final UserService userService, final MyMovieService myMovieService) {
        this.userService = userService;
        this.myMovieService = myMovieService;
    }

    @GetMapping
    public MoviesDto findMyMovies(@RequestParam(name = "filter", required = false) final MovieFilter filter,
            final Principal principal) {
        final User user = extractUserFromPrincipal(principal).get();

        Optional<MovieFilter> optionalFilter = filter != null ? Optional.of(filter) : Optional.empty();
        final MoviesDto movies = myMovieService.findMyMovies(user, optionalFilter);
        return movies;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
