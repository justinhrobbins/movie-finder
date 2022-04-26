import { React, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { ActorMovieCard } from './ActorMovieCard';
import Select from 'react-select';

import './ActorMovieListCard.scss';

export const ActorMovieListCard = ({ actor }) => {
    const [loading, setLoading] = useState(true);
    const [personCredits, setPersonCredits] = useState({});
    const [unfilteredPersonCredits, setUnfilteredPersonCreditsPersonCredits] = useState({});
    useEffect(
        () => {
            const fetchPersonCredits = async () => {
                try {
                    setLoading(true);
                    const response = await fetch(`http://localhost:8080/person/${actor.id}/movies`);
                    const movieData = await response.json();
                    setPersonCredits(movieData);
                    setUnfilteredPersonCreditsPersonCredits(JSON.parse(JSON.stringify(movieData)));
                } catch (error) {
                    throw error;
                } finally {
                    setLoading(false);
                }
            };
            fetchPersonCredits();
        }, []
    );

    const [searchParams] = useSearchParams();
    const location = useLocation();
    const [selectedSortOption, setSelectedSortOption] = useState({ value: 'popularity', label: 'Popularity' });
    const [selectedFilterOption, setSelectedFilterOption] = useState({ value: 'all', label: 'All Movies' });
    const sortOptions = [
        { value: 'popularity', label: 'Popularity' },
        { value: 'newest', label: 'Release Date (Newest)' },
        { value: 'oldest', label: 'Release Date (Oldest)' }
    ]
    const filterOptions = [
        { value: 'all', label: 'All Movies' },
        { value: 'upcoming', label: 'Upcoming Releases' },
        { value: 'recent', label: 'Recent Release' }
    ]
    useEffect(
        () => {
            const sortParam = searchParams.get('sort');
            const filterParam = searchParams.get('filter');
            const sortOption = sortOptions.find(option => option.value === sortParam);
            const filterOption = filterOptions.find(option => option.value === filterParam);

            if (sortOption) {
                setSelectedSortOption(sortOption);
            }
            if (filterOption) {
                setSelectedFilterOption(filterOption);
            }
        }, [location]
    );

    useEffect(
        () => {
            const filterMovies = (allMovies) => {
                const today = new Date();
                const minNewReleaseDate = new Date(new Date().setFullYear(new Date().getFullYear() - 1));

                let filteredCredits;
                if (selectedFilterOption.value == "all") {
                    filteredCredits = allMovies.cast;
                } else if (selectedFilterOption.value == "upcoming") {
                    filteredCredits = allMovies.cast.filter(function (el) {
                        return new Date(el.release_date) > today;
                    });
                } else {
                    filteredCredits = allMovies.cast.filter(function (el) {
                        const releaseDate = new Date(el.release_date);
                        return releaseDate < today && releaseDate > minNewReleaseDate;
                    });
                }
                return filteredCredits;
            };

            const sortMovies = (movies) => {
                let sortedMovies;
                if (selectedSortOption.value == "newest") {
                    sortedMovies = movies.sort((a, b) => new Date(b.release_date) - new Date(a.release_date));
                } else if (selectedSortOption.value == "oldest") {
                    sortedMovies = movies.sort((a, b) => new Date(a.release_date) - new Date(b.release_date));
                } else {
                    sortedMovies = movies.sort((a, b) => b.popularity - a.popularity);
                }
                return sortedMovies;
            }

            if (unfilteredPersonCredits.cast) {
                const filteredMovies = filterMovies(unfilteredPersonCredits);
                const sortedMovies = sortMovies(filteredMovies);

                personCredits.cast = sortedMovies;
                setPersonCredits({ ...personCredits });
            }

        }, [selectedSortOption, selectedFilterOption, loading, location]
    );

    const handleSortChange = (selectedOption) => {
        setSelectedSortOption(selectedOption);
    };

    const handleFilterChange = (selectedOption) => {
        setSelectedFilterOption(selectedOption);
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
                <div className="actor-movie-list-sort">
                    Sort by:
                    <Select
                        onChange={handleSortChange}
                        options={sortOptions}
                        value={selectedSortOption}
                        styles={colourStyles}
                    />
                </div>
                <div className="actor-movie-list-filter">Flter by:
                    <Select
                        onChange={handleFilterChange}
                        options={filterOptions}
                        styles={colourStyles}
                        value={selectedFilterOption}
                    />
                </div>
            </div>

            {personCredits.cast
                .map(movie => <ActorMovieCard key={movie.id} movie={movie} />)
            }
        </div>
    );
}