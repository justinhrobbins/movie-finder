package org.robbins.moviefinder.converters;

import org.robbins.moviefinder.enums.MovieSort;
import org.springframework.core.convert.converter.Converter;

public class MovieSortConverter implements Converter<String, MovieSort> {

    @Override
    public MovieSort convert(final String source) {
        return MovieSort.valueOf(source.toUpperCase());
    }
}
