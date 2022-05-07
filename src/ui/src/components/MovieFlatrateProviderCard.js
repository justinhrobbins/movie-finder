import { React } from 'react';

import './scss/MovieFlatrateProviderCard.scss';

export const MovieFlatrateProviderCard = ({ flatrateProvider }) => {
    if (!flatrateProvider) return null;

    const baseUrl = "https://image.tmdb.org/t/p/w45/";
    const defaultMovieUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const providerUrl = !flatrateProvider.logo_path || flatrateProvider.logo_path === "" ? defaultMovieUrl : baseUrl + flatrateProvider.logo_path;

    return (
        <div className="MovieFlatrateProviderCard">
            <div className="movie-flatrate-provider-card-image"><img alt={flatrateProvider.provider_name} title={flatrateProvider.provider_name} src={providerUrl} /></div>
        </div>
    );
}