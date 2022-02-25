import { React } from 'react';

export const MovieFlatrateProviderCard = ({ flatrateProvider }) => {
    if (!flatrateProvider) return null;

    const baseUrl = "https://image.tmdb.org/t/p/w45/";
    const defaultMovieUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const providerUrl = !flatrateProvider.logo_path || flatrateProvider.logo_path === "" ? defaultMovieUrl : baseUrl + flatrateProvider.logo_path;

    return (
        <div className="MovieFlatrateProviderCard">
            <p>{flatrateProvider.provider_name}</p>
            <img src={providerUrl} />
        </div>
    );
}