package org.robbins.moviefinder.filters;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class MovieFinderRequestLoggingFilter extends AbstractRequestLoggingFilter {

    @Override
    protected void beforeRequest(HttpServletRequest httpServletRequest, String message) {
        this.logger.debug(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest httpServletRequest, String message) {
        // no-op implementation
    }
}