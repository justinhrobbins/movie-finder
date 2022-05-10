import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { MyMovieActorCard } from '../components/MyMovieActorCard';

import './scss/MyMoviesPage.scss';

export const MyMoviesPage = () => {
    const { loggedInUser } = useContext(UserContext);
    const [movies, setMovies] = useState();

    useEffect(
        () => {
            const fetchMovies = async () => {
                try {
                  const response = await fetch(process.env.REACT_APP_BACKEND_URL + 'actoralerts/movies?filter=RECENT', {
                    method: 'GET',
                    headers: {
                      'Authorization': `Bearer ${loggedInUser.tokenId}`
                    }
                  });
                  const movies = await response.json();
                  setMovies((movies));
                } catch (error) {
                  throw error;
                } finally {
                }
              };

            fetchMovies();
        }, []
    );

    if (!loggedInUser) {
        return <h3>Login to view Movies from your Actors</h3>
      }

    if (!movies || !movies.actors) {
        return <span>Searching for movies for your actors</span>
    }

    return (
        <div className="MyMoviesPage">
            <div className="my-movies-page-about-container">
                <div className="my-movies-page-about-intro">
                    Movies from the actors you follow
                    <br/><br/>
                    Use Actor Alerts to:
                </div>
                <ul className="my-movies-page-about-list">
                    <li>Quickly find <b>upcoming movies</b> from your favorite actors</li>
                </ul>
            </div>
            {
                movies.actors
                    .map(actor => <MyMovieActorCard key={actor.actorId} actor={actor} />)
            }
        </div>
    );
}