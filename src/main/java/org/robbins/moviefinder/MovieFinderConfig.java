package org.robbins.moviefinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import info.movito.themoviedbapi.TmdbApi;

@Configuration
public class MovieFinderConfig {
    final TmdbConfigProperties tmdbProperties;
    
    public MovieFinderConfig(final TmdbConfigProperties tmdbProperties) {
        this.tmdbProperties = tmdbProperties;
    }
    
    @Bean
    public TmdbApi getTmdbApi() {
        return new TmdbApi(tmdbProperties.getApikey());
    }
}
