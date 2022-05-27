package org.robbins.moviefinder.converters;

import org.robbins.moviefinder.enums.ActorSort;
import org.springframework.core.convert.converter.Converter;

public class ActorSortConverter implements Converter<String, ActorSort> {

    @Override
    public ActorSort convert(final String source) {
        return ActorSort.valueOf(source.toUpperCase());
    }
}
