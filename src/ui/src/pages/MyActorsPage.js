import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorSummaryCard } from '../components/ActorSummaryCard';
import { ActorDetailCard } from '../components/ActorDetailCard';
import Select from 'react-select';

import './scss/MyActorsPage.scss';

export const MyActorsPage = () => {
  const { loggedInUser } = useContext(UserContext);
  const [myActors, setMyActors] = useState({});
  const [unfollowedActor, setUnfollowedActor] = useState({});

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
      const fetchMyActors = async () => {
        try {
          const response = await fetch(process.env.REACT_APP_BACKEND_URL + `myactors?filter=${filterParam}&sort=${sortParam}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${loggedInUser.tokenId}`
            }
          });
          const actors = await response.json();
          setMyActors(actors);
        } catch (error) {
          throw error;
        } finally {
        }
      };

      if (loggedInUser) {
        fetchMyActors();
      }

      const sortOption = sortOptions.find(option => option.value === sortParam);
      const filterOption = filterOptions.find(option => option.value === filterParam);
      if (sortOption) {
        setSelectedSortOption(sortOption);
      }
      if (filterOption) {
        setSelectedFilterOption(filterOption);
      }
    }, [loggedInUser, location, unfollowedActor]
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

  const notifyOnActorUnfollow = (unfollowedActor) => {
    setUnfollowedActor(unfollowedActor);
  }

  if (!loggedInUser) {
    return <h3>Login to configure your Actors</h3>
  }

  if (!myActors || !myActors.actors) {

    return <h3>Loading your Actors...</h3>
  }

  if (myActors.actorCounts.actorCount === 0) {
    return <h3>You are not following any Actors</h3>
  }

  return (
    <div className="MyActorsPage">
      <div className="my-actors-header-container">
        <div className="my-actors-header-dashboard">
          <ActorSummaryCard actorCounts={myActors.actorCounts} movieCounts={myActors.movieCounts} />
        </div>
        <div className="my-actors-dropdown-filter">Flter by:
          <Select
            onChange={handleFilterChange}
            options={filterOptions}
            styles={colourStyles}
            value={selectedFilterOption}
          />
        </div>
        <div className="my-actors-dropdown-sort">
          Sort by:
          <Select
            onChange={handleSortChange}
            options={sortOptions}
            value={selectedSortOption}
            styles={colourStyles}
          />
        </div>
      </div>
      <div className="my-actors-label">
        <h2>My Actors:</h2>
      </div>
      <div className="my-actors-actors-container">
        {myActors.actors
          .map(actor =>
            <div className="ActorDetailCard">
              <ActorDetailCard key={actor.actorId} providedActor={actor} removeActor={notifyOnActorUnfollow} showActorBio={false} />
            </div>
          )
        }
      </div>
    </div>
  );
}