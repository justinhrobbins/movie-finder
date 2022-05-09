package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.people.PersonCredit;

public class MoviesDto {

    private List<PersonCredit> movies = new ArrayList<>();
    private int upcomingMovieCount;
    private int recentMovieCount;
    private int subscriptionCount;

    public MoviesDto() {
    }

    public List<PersonCredit> getMovies() {
        return movies;
    }

    public void setMovies(List<PersonCredit> movies) {
        this.movies = movies;
    }

    public int getUpcomingMovieCount() {
        return upcomingMovieCount;
    }

    public void setUpcomingMovieCount(int upcomingMovieCount) {
        this.upcomingMovieCount = upcomingMovieCount;
    }

    public int getRecentMovieCount() {
        return recentMovieCount;
    }

    public void setRecentMovieCount(int recentMovieCount) {
        this.recentMovieCount = recentMovieCount;
    }

    public int getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(int subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }
}
