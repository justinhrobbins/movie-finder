package org.robbins.moviefinder.config;

import org.robbins.moviefinder.converters.ActorSortConverter;
import org.robbins.moviefinder.converters.MovieFilterConverter;
import org.robbins.moviefinder.converters.MovieSortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MovieFilterConverter());
        registry.addConverter(new MovieSortConverter());
        registry.addConverter(new ActorSortConverter());
    }
}
