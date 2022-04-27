import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorAlertDetailCard } from '../components/ActorAlertDetailCard';
import { ActortAlertSummaryCard } from '../components/ActortAlertSummaryCard';
import Select from 'react-select';

import './ActorAlertsPage.scss';

export const ActorAlertsPage = () => {
  const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);
  const [loading, setLoading] = useState(true);
  const [userActorAlerts, setUserActorAlerts] = useState({});
  const [unfilteredUserActorAlerts, setUnfilteredUserActorAlerts] = useState({});

  useEffect(
    () => {
      const fetchActorAlerts = async () => {
        try {
          setLoading(true);
          const response = await fetch('http://localhost:8080/actoralerts', {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${loggedInUser.tokenId}`
            }
          });
          const userActorAlerts = await response.json();
          setUserActorAlerts(userActorAlerts);
          setUnfilteredUserActorAlerts(JSON.parse(JSON.stringify(userActorAlerts)));
        } catch (error) {
          throw error;
        } finally {
          setLoading(false);
        }
      };

      if (loggedInUser) {
        fetchActorAlerts();
      }
    }, []
  );

  const [searchParams] = useSearchParams();
  const location = useLocation();
  const [selectedSortOption, setSelectedSortOption] = useState({ value: 'name', label: 'Name' });
  const [selectedFilterOption, setSelectedFilterOption] = useState({ value: 'all', label: 'All Actors' });
  const sortOptions = [
    { value: 'name', label: 'Name' },
    { value: 'upcoming', label: 'Upcoming Releases' },
    { value: 'recent', label: 'Recent Releases' },
    { value: 'total', label: 'Total Releases' }
  ]
  const filterOptions = [
    { value: 'all', label: 'No filter' },
    { value: 'upcoming', label: 'Has Upcoming Releases' },
    { value: 'recent', label: 'Has Recent Releases' }
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
          filteredActors = allActorAlerts.actorAlerts.filter(function (el) {
            return el.details.recentMovies > 0;
          });
        } else if (selectedFilterOption.value == "upcoming") {
          filteredActors = allActorAlerts.actorAlerts.filter(function (el) {
            return el.details.upcomingMovies > 0;
          });
        } else {
          filteredActors = allActorAlerts.actorAlerts;
        }
        return filteredActors;
      };

      const sortActors = (actorAlerts) => {
        let sortedActorAlerts;
        if (selectedSortOption.value == "name") {
          sortedActorAlerts = actorAlerts.sort((a, b) => a.person.name.localeCompare(b.person.name));
        } else if (selectedSortOption.value == "upcoming") {
          sortedActorAlerts = actorAlerts.sort((a, b) => b.details.upcomingMovies - a.details.upcomingMovies);
        } else if (selectedSortOption.value == "recent") {
          sortedActorAlerts = actorAlerts.sort((a, b) => b.details.recentMovies - a.details.recentMovies);
        } else {
          sortedActorAlerts = actorAlerts.sort((a, b) => b.details.totalMovies - a.details.totalMovies);
        }
        return sortedActorAlerts;
      }

      if (unfilteredUserActorAlerts.actorAlerts) {
        const filteredActors = filterActors(unfilteredUserActorAlerts);
        const sortedActors = sortActors(filteredActors);

        userActorAlerts.actorAlerts = sortedActors;
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

  if (!userActorAlerts || !userActorAlerts.actorAlerts || userActorAlerts.actorAlerts.length === 0) {

    return <h3>Loading your Actor Alerts...</h3>
  }

  return (
    <div className="ActorAlertsPage">
      <div className="actor-alert-movie-list-section">
        <div className="actor-alert-movie-list-section-summary">
          <div className="actor-alert-movie-list-section-summary-data">
                    <ActortAlertSummaryCard userActorAlerts={userActorAlerts} />
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
        {userActorAlerts.actorAlerts
          .map(actorAlert => <ActorAlertDetailCard key={actorAlert.actorId} providedActor={actorAlert.person} actorDetails={actorAlert.details} />)
        }
      </div>
    </div>
  );
}