import { React, useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { UserContext } from "../UserContext";

import './scss/MovieCastCard.scss';

export const MovieCastCard = ({ providedMovie }) => {
    const { loggedInUser } = useContext(UserContext);
    const [movie, setMovie] = useState(providedMovie);
    const [actorsInMovie, setActorsInMovie] = useState(null);

    useEffect(
        () => {
            setMovie(providedMovie);
        }, [providedMovie]
    );

    useEffect(
        () => {
            const fetchActorsInMovie = async () => {
                try {
                    const response = await fetch(process.env.REACT_APP_BACKEND_URL + `movies/${movie.id}/cast`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${loggedInUser.tokenId}`
                        }
                    });
                    const actors = await response.json();
                    setActorsInMovie(actors);
                } catch (error) {
                    throw error;
                } finally {
                }
            };

            if (movie) {
                fetchActorsInMovie();
            }

        }, [movie, loggedInUser]
    );

    const actorProfileUrl = (profilePath) => {
        const baseUrl = "https://image.tmdb.org/t/p/" + "w45" + "/";
        const defaultProfileUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
        return !profilePath || profilePath === "" || profilePath === null
            ? defaultProfileUrl
            : (baseUrl + profilePath);
    };

    const actorLinkUrl = (actorId) => {
        return "/actors/" + actorId;
     }

    if (!actorsInMovie) {
        return null;
    }

    return (
        <div className="MovieCastCard">
            {actorsInMovie.slice(0,8).map((actor, index) =>
                <Link className="movie-cast-link" to={actorLinkUrl(actor.id)}>
                    <img src={actorProfileUrl(actor.profile_path)} alt={actor.name} title={actor.name} />
                </Link>
            )
            }
        </div>
    );
}