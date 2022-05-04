package org.robbins.moviefinder.dtos;

public class ActorMovieSubscriptionCountsDto {
    String subcriptionService;
    int movieCount = 0;

    public ActorMovieSubscriptionCountsDto(String subcriptionService, int movieCount) {
        this.subcriptionService = subcriptionService;
        this.movieCount = movieCount;
    }

    public String getSubcriptionService() {
        return subcriptionService;
    }
    public int getMovieCount() {
        return movieCount;
    }

    public void setSubcriptionService(String subcriptionService) {
        this.subcriptionService = subcriptionService;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    
}