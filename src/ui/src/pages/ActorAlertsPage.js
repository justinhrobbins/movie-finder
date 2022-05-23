import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorAlertSummaryCard } from '../components/ActorAlertSummaryCard';
import { ActorAlertDetailCard } from '../components/ActorAlertDetailCard';
import Select from 'react-select';

import './scss/ActorAlertsPage.scss';

export const ActorAlertsPage = () => {
  const { loggedInUser } = useContext(UserContext);
  const [userActorAlerts, setUserActorAlerts] = useState({});

  const [searchParams, setSearchParams] = useSearchParams();
  const location = useLocation();
  const filterParam = searchParams.get('filter') ? searchParams.get('filter') : '';
  const sortParam = searchParams.get('sort') ? searchParams.get('sort') : '';
  const [selectedSortOption, setSelectedSortOption] = useState({ value: 'popularity', label: 'Popularity' });
  const [selectedFilterOption, setSelectedFilterOption] = useState({ value: 'all', label: 'All My Actors' });
  const sortOptions = [
    { value: 'name', label: 'Name' },
    { value: 'popularity', label: 'Popularity' },
    { value: 'upcoming', label: 'Upcoming Movies' },
    { value: 'recent', label: 'Recent Movies' },
    { value: 'subscription', label: 'Movies on my subscriptions' },
    { value: 'total', label: 'Total Movies' }
  ]
  const filterOptions = [
    { value: 'all', label: 'All My Actors' },
    { value: 'upcoming', label: 'Actors with upcoming releases' },
    { value: 'recent', label: 'Actors with recent releases' },
    { value: 'subscriptions', label: 'Actors with movies on my subscriptions' }
  ]

  useEffect(
    () => {
      const fetchActorAlerts = async () => {
        try {
          const response = await fetch(process.env.REACT_APP_BACKEND_URL + `actoralerts?filter=${filterParam}&sort=${sortParam}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${loggedInUser.tokenId}`
            }
          });
          const actors = await response.json();
          setUserActorAlerts(actors);
        } catch (error) {
          throw error;
        } finally {
        }
      };

      if (loggedInUser) {
        fetchActorAlerts();
      }

      const sortOption = sortOptions.find(option => option.value === sortParam);
      const filterOption = filterOptions.find(option => option.value === filterParam);
      if (sortOption) {
        setSelectedSortOption(sortOption);
      }
      if (filterOption) {
        setSelectedFilterOption(filterOption);
      }
    }, [loggedInUser, location]
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

  if (!loggedInUser) {
    return <h3>Login to configure your Actors</h3>
  }

  if (!userActorAlerts || !userActorAlerts.actors) {

    return <h3>Loading your Actors...</h3>
  }

  if (userActorAlerts.actorCounts.actorCount === 0) {
    return <h3>You are not following any Actors</h3>
  }

  return (
    <div className="ActorAlertsPage">
      <div className="actor-alert-header">
        <h2>My Actors:</h2>
      </div>
      <div className="actor-alert-movie-list-section">
        <div className="actor-alert-movie-list-section-summary">
          <div className="actor-alert-movie-list-section-summary-data">
            <ActorAlertSummaryCard actors={userActorAlerts} />
          </div>
        </div>
        <div className="actor-alert-movie-list-filter">Flter by:
          <Select
            onChange={handleFilterChange}
            options={filterOptions}
            styles={colourStyles}
            value={selectedFilterOption}
          />
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
      </div>
      {
        !loggedInUser ? "Please login to create Actor Alerts" : ""
      }
      <div className="actor-alerts-page-actors-list">
        {userActorAlerts.actors
          .map(actor => <ActorAlertDetailCard key={actor.actorId} providedActor={actor} />)
        }
      </div>
    </div>
  );
}