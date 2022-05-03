import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorMovieCard } from './ActorMovieCard';
import Select from 'react-select';

import './ActorMovieListCard.scss';

export const ActorMovieListCard = ({ actor }) => {
    const { loggedInUser } = useContext(UserContext);
    const [loading, setLoading] = useState(true);
    const [sortedAndFiltered, setSortedAndFiltered] = useState(false);
    const [personCredits, setPersonCredits] = useState({});
    const [unfilteredPersonCredits, setUnfilteredPersonCredits] = useState({});
    useEffect(
        () => {
            const fetchPersonCredits = async () => {
                try {
                    setLoading(true);
                    const response = await fetch(`http://localhost:8080/person/${actor.id}/movies`);
                    const movieData = await response.json();
                    setUnfilteredPersonCredits(movieData);
                } catch (error) {
                    throw error;
                } finally {
                    setLoading(false);
                }
            };
            fetchPersonCredits();
        }, [actor]
    );

    const [searchParams, setSearchParams] = useSearchParams();
    const location = useLocation();
    const [sortAndFilterOptions, setSortAndFilterOptions] = useState({
        "filterOption": {
            "value": 'all', "label": "All Movies"
        },
        "sortOption": {
            "value": "popularity", "label": "Popularity"
        }
    });
    const sortOptions = [
        { value: 'popularity', label: 'Popularity' },
        { value: 'newest', label: 'Release Date (Newest)' },
        { value: 'oldest', label: 'Release Date (Oldest)' }
    ]
    const filterOptions = [
        { value: 'all', label: 'All Movies' },
        { value: 'upcoming', label: 'Upcoming Releases' },
        { value: 'recent', label: 'Recent Release' },
        { value: 'subscriptions', label: 'My Subscriptions' }
    ]
    useEffect(
        () => {
            setSortedAndFiltered(false);
            
            const sortParam = searchParams.get('sort');
            const filterParam = searchParams.get('filter');
            const sortOption = sortOptions.find(option => option.value === sortParam);
            const filterOption = filterOptions.find(option => option.value === filterParam);

            if (sortOption) {
                sortAndFilterOptions.sortOption = sortOption;
            }
            if (filterOption) {
                sortAndFilterOptions.filterOption = filterOption;
            }

            setSortAndFilterOptions({...sortAndFilterOptions});
        }, [location, loggedInUser]
    );

    useEffect(
        () => {
            const filterMovies = (allMovies) => {
                const today = new Date();
                const minNewReleaseDate = new Date(new Date().setFullYear(new Date().getFullYear() - 1));
                let filteredCredits;
                if (sortAndFilterOptions.filterOption.value == "all") {
                    filteredCredits = allMovies.cast;
                } else if (sortAndFilterOptions.filterOption.value == "upcoming") {
                    filteredCredits = allMovies.cast.filter(function (el) {
                        return new Date(el.release_date) > today;
                    });
                } else if (sortAndFilterOptions.filterOption.value == "recent") {
                    filteredCredits = allMovies.cast.filter(function (el) {
                        const releaseDate = new Date(el.release_date);
                        return releaseDate < today && releaseDate > minNewReleaseDate;
                    });
                } else if (sortAndFilterOptions.filterOption.value == "subscriptions") {
                    filteredCredits = allMovies.cast;
                }
                return filteredCredits;
            };

            const sortMovies = (movies) => {
                let sortedMovies;
                if (sortAndFilterOptions.sortOption.value == "newest") {
                    sortedMovies = movies.sort((a, b) => new Date(b.release_date) - new Date(a.release_date));
                } else if (sortAndFilterOptions.sortOption.value == "oldest") {
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
                setSortedAndFiltered(true);
            }

        }, [sortAndFilterOptions, loading]
    );

    const handleSortChange = (selectedOption) => {
        setSortedAndFiltered(false);
        let updatedSearchParams = new URLSearchParams(searchParams.toString());
        updatedSearchParams.set('sort', selectedOption.value);
        setSearchParams(updatedSearchParams.toString());
    };

    const handleFilterChange = (selectedOption) => {
        setSortedAndFiltered(false);
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
        }),
        singleValue: (provided, { data }) => ({
            ...provided,
            color: 'white',
        }),
    }

    if ((!personCredits || !personCredits.cast) || (sortedAndFiltered == false)) {
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
                        value={sortAndFilterOptions.sortOption}
                        styles={colourStyles}
                    />
                </div>
                <div className="actor-movie-list-filter">Flter by:
                    <Select
                        onChange={handleFilterChange}
                        options={filterOptions}
                        styles={colourStyles}
                        value={sortAndFilterOptions.filterOption}
                    />
                </div>
            </div>
            {
                personCredits.cast
                    .map(movie => <ActorMovieCard
                        key={movie.id}
                        providedMovie={movie}
                        filterBySubscriptions={sortAndFilterOptions.filterOption.value == "subscriptions" ? true : false} />)
            }
        </div>
    );
}