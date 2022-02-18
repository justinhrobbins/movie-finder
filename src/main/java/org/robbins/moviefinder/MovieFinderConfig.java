package org.robbins.moviefinder;

import org.robbins.moviefinder.filters.MovieFinderRequestLoggingFilter;
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

    @Bean
    public MovieFinderRequestLoggingFilter logFilter() {
        MovieFinderRequestLoggingFilter filter = new MovieFinderRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(false);
        filter.setBeforeMessagePrefix("Request received: ");
        return filter;
    }
}
