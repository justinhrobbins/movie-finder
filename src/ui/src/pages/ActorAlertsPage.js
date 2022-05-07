import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorAlertSummaryCard } from '../components/ActorAlertSummaryCard';
import { ActorAlertDetailCard } from '../components/ActorAlertDetailCard';
import Select from 'react-select';

import './ActorAlertsPage.scss';

export const ActorAlertsPage = () => {
  const { loggedInUser } = useContext(UserContext);
  const [loading, setLoading] = useState(true);
  const [userActorAlerts, setUserActorAlerts] = useState({});
  const [unfilteredUserActorAlerts, setUnfilteredUserActorAlerts] = useState({});

  useEffect(
    () => {
      const fetchActorAlerts = async () => {
        try {
          setLoading(true);
          const response = await fetch(process.env.REACT_APP_BACKEND_URL + 'actoralerts', {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${loggedInUser.tokenId}`
            }
          });
          const userActorAlerts = await response.json();
          setUnfilteredUserActorAlerts((userActorAlerts));
        } catch (error) {
          throw error;
        } finally {
          setLoading(false);
        }
      };

      if (loggedInUser) {
        fetchActorAlerts();
      }
    }, [loggedInUser]
  );

  const [searchParams] = useSearchParams();
  const location = useLocation();
  const [selectedSortOption, setSelectedSortOption] = useState({ value: 'popularity', label: 'Popularity' });
  const [selectedFilterOption, setSelectedFilterOption] = useState({ value: 'all', label: 'All Actors' });
  const sortOptions = [
    { value: 'name', label: 'Name' },
    { value: 'popularity', label: 'Popularity' },
    { value: 'upcoming', label: 'Upcoming Releases' },
    { value: 'recent', label: 'Recent Releases' },
    { value: 'total', label: 'Total Releases' }
  ]
  const filterOptions = [
    { value: 'all', label: 'No filter' },
    { value: 'upcoming', label: 'Actors with upcoming releases' },
    { value: 'recent', label: 'Actors with recent releases' },
    { value: 'subscriptions', label: 'Actors with movies on my subscriptions' }
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
      const filterActors = (allActorAlerts) => {
        let filteredActors;
        if (selectedFilterOption.value == "recent") {
          filteredActors = allActorAlerts.actors.filter(function (el) {
            return el.movieCounts.recentMovies > 0;
          });
        } else if (selectedFilterOption.value == "upcoming") {
          filteredActors = allActorAlerts.actors.filter(function (el) {
            return el.movieCounts.upcomingMovies > 0;
          });
        } else if (selectedFilterOption.value == "subscriptions") {
          filteredActors = allActorAlerts.actors.filter(function (actor) {
            return actorHasMoviesOnSubscriptions(actor);
          });
        } else {
          filteredActors = allActorAlerts.actors;
        }
        return filteredActors;
      };

      const actorHasMoviesOnSubscriptions = (actor) => {
        const hasSubscriptions = actor.movieCounts.subscriptions
          .some(subscription => loggedInUser.streamingServices.includes(subscription.subcriptionService))
        return hasSubscriptions;
      }

      const sortActors = (actorAlerts) => {
        let sortedActorAlerts;
        if (selectedSortOption.value == "popularity") {
          sortedActorAlerts = actorAlerts.sort((a, b) => b.person.popularity - a.person.popularity);
        } else if (selectedSortOption.value == "name") {
          sortedActorAlerts = actorAlerts.sort((a, b) => a.person.name.localeCompare(b.person.name));
        } else if (selectedSortOption.value == "upcoming") {
          sortedActorAlerts = actorAlerts.sort((a, b) => b.movieCounts.upcomingMovies - a.movieCounts.upcomingMovies);
        } else if (selectedSortOption.value == "recent") {
          sortedActorAlerts = actorAlerts.sort((a, b) => b.movieCounts.recentMovies - a.movieCounts.recentMovies);
        } else {
          sortedActorAlerts = actorAlerts.sort((a, b) => b.movieCounts.totalMovies - a.movieCounts.totalMovies);
        }
        return sortedActorAlerts;
      }

      if (unfilteredUserActorAlerts.actors) {
        const filteredActors = filterActors(unfilteredUserActorAlerts);
        const sortedActors = sortActors(filteredActors);

        userActorAlerts.actors = sortedActors;
        setUserActorAlerts({ ...userActorAlerts });
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

  if (!loggedInUser) {
    return <h3>Login to configure your Actor Alerts</h3>
  }

  if (!userActorAlerts || !userActorAlerts.actors) {

    return <h3>Loading your Actor Alerts...</h3>
  }

  if (unfilteredUserActorAlerts.actors.length === 0) {

    return <h3>You have no Actor Alerts configured</h3>
  }

  return (
    <div className="ActorAlertsPage">
      <div className="actor-alert-header">
        <h2>My Actors:</h2>
      </div>
      <div className="actor-alert-movie-list-section">
        <div className="actor-alert-movie-list-section-summary">
          <div className="actor-alert-movie-list-section-summary-data">
            <ActorAlertSummaryCard userActorAlerts={unfilteredUserActorAlerts} />
          </div>
        </div>
        <div className="actor-alert-movie-list-sort">
          Sort by:
          <Select
            onChange={handleSortChange}
            options={sortOptions}
            value={selectedSortOption}
            styles={colourStyles}
          />
        </div>
        <div className="actor-alert-movie-list-filter">Flter by:
          <Select
            onChange={handleFilterChange}
            options={filterOptions}
            styles={colourStyles}
            value={selectedFilterOption}
          />
        </div>
      </div>
      {
        !loggedInUser ? "Please login to create Actor Alerts" : ""
      }
      <div className="actor-alerts-page-actors-list">
        {userActorAlerts.actors
          .map(actors => <ActorAlertDetailCard key={actors.actorId} providedActor={actors.person} actorDetails={actors.movieCounts} />)
        }
      </div>
    </div>
  );
}