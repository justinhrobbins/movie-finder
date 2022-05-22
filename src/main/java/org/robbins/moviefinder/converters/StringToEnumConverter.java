package org.robbins.moviefinder.converters;

import org.robbins.moviefinder.dtos.Filters;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, Filters> {

    @Override
    public Filters convert(final String source) {
        return Filters.valueOf(source.toUpperCase());
    }
}
