package org.robbins.moviefinder.converters;

import org.robbins.moviefinder.enums.MovieFilter;
import org.springframework.core.convert.converter.Converter;

public class MovieFilterConverter implements Converter<String, MovieFilter> {

    @Override
    public MovieFilter convert(final String source) {
        return MovieFilter.valueOf(source.toUpperCase());
    }
}
