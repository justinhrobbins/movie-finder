import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { MovieCard } from './MovieCard';
import Select from 'react-select';

import './scss/ActorMovieListCard.scss';

export const ActorMovieListCard = ({ actor }) => {
    const { loggedInUser } = useContext(UserContext);
    const [personCredits, setPersonCredits] = useState({});

    const location = useLocation();
    const [searchParams, setSearchParams] = useSearchParams();
    const sortParam = searchParams.get('sort') ? searchParams.get('sort') : '';
    const filterParam = searchParams.get('filter') ? searchParams.get('filter') : '';
    const [selectedSortOption, setSelectedSortOption] = useState({ value: 'popularity', label: 'Popularity' });
    const [selectedFilterOption, setSelectedFilterOption] = useState({ value: 'all', label: 'All Movies' });
    const sortOptions = [
        { value: 'popularity', label: 'Popularity' },
        { value: 'newest', label: 'Release Date (Newest)' },
        { value: 'oldest', label: 'Release Date (Oldest)' }
    ]
    const filterOptions = [
        { value: 'all', label: 'All Movies' },
        { value: 'upcoming', label: 'Upcoming releases' },
        { value: 'recent', label: 'Recent release' },
        { value: 'subscriptions', label: 'Movies on my subscriptions' }
    ]

    useEffect(
        () => {
            const fetchPersonCredits = async () => {
                try {
                    var headers = new Headers();
                    headers.append("Content-Type", "application/json");

                    if (loggedInUser) {
                        headers.append("Authorization", `Bearer ${loggedInUser.tokenId}`);
                    }
                    const requestOptions = {
                        method: 'GET',
                        headers: headers
                    };

                    const response = await fetch(process.env.REACT_APP_BACKEND_URL + `actors/${actor.id}/movies?filter=${filterParam}&sort=${sortParam}`,
                        requestOptions);
                    const movieData = await response.json();
                    setPersonCredits(movieData);
                } catch (error) {
                    throw error;
                } finally {
                }
            };
            fetchPersonCredits();

            const sortOption = sortOptions.find(option => option.value === sortParam);
            const filterOption = filterOptions.find(option => option.value === filterParam);

            if (sortOption) {
                setSelectedSortOption(sortOption);
            }
            if (filterOption) {
                setSelectedFilterOption(filterOption);
            }
        }, [actor, location]
    );

    const handleSortChange = (selectedOption) => {
        let updatedSearchParams = new URLSearchParams(searchParams.toString());
        updatedSearchParams.set('sort', selectedOption.value);
        setSearchParams(updatedSearchParams.toString());
    };

    const handleFilterChange = (selectedOption) => {
        let updatedSearchParams = new URLSearchParams(searchParams.toString());
        updatedSearchParams.set('filter', selectedOption.value);
        setSearchParams(updatedSearchParams.toString());
    };

    const colourStyles = {
        option: (base, state) => ({
            ...base,
            backgroundColor: state.isFocused ? "#808080" : "#A9A9A9",
            borderBottom: "1px solid black",
        }),
        control: (styles) => ({
            ...styles,
            backgroundColor: "#181a1e",
            minHeight: 36,
            width: "200px"
        }),
        singleValue: (provided, { data }) => ({
            ...provided,
            color: 'white',
        }),
    }

    if (!personCredits || !personCredits.cast) {
        return <span>Searching for movies for {actor.name}</span>
    }

    return (
        <div className="ActorMovieListCard">
            <div className="actor-movie-list-section">
                <h2 className="actor-movie-list-label">Movies for {actor.name}</h2>
                <div className="actor-movie-list-filter">Flter by:
                    <Select
                        onChange={handleFilterChange}
                        options={filterOptions}
                        styles={colourStyles}
                        value={selectedFilterOption}
                    />
                </div>
                <div className="actor-movie-list-sort">
                    Sort by:
                    <Select
                        onChange={handleSortChange}
                        options={sortOptions}
                        value={selectedSortOption}
                        styles={colourStyles}
                    />
                </div>
            </div>
            <div className="actor-movie-list-container">
                {
                    personCredits.cast
                        .map(movie =>
                            <div key={movie.id} className="actor-movie-list-item">
                                <MovieCard
                                    key={movie.id}
                                    providedMovie={movie}
                                    shouldShowFullOverview={false} />
                            </div>
                        )
                }
            </div>
        </div>
    );
}