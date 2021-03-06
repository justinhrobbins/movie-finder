import { React, useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { FaCheckCircle, FaCircle } from 'react-icons/fa';
import { UserContext } from "../UserContext";

import './scss/MovieCast.scss';

export const MovieCast = ({ providedMovie }) => {
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
                    var headers = new Headers();
                    headers.append("Content-Type", "application/json");

                    if (loggedInUser) {
                        headers.append("Authorization", `Bearer ${loggedInUser.tokenId}`);
                    }

                    const requestOptions = {
                        method: 'GET',
                        headers: headers
                    };

                    const response = await fetch(process.env.REACT_APP_BACKEND_URL + `movies/${movie.id}/cast`,
                        requestOptions);

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
        <div className="MovieCast">
            {actorsInMovie.slice(0, 5).map((actor) =>
                <div className="movie-cast-item" key={actor.actorId} >
                    <Link key={actor.actorId} className="movie-cast-link" to={actorLinkUrl(actor.actorId)}>
                        <img key={actor.actorId} src={actorProfileUrl(actor.person.profile_path)} alt={actor.person.name} title={actor.person.name} />
                        {actor.followedByUser == true &&
                            <FaCircle size=".55em" color="white" style={{ position: 'absolute', top: '-3', right: '0' }} />
                        }
                        {actor.followedByUser == true &&
                            <FaCheckCircle size=".75em"
                                color="cornflowerblue"
                                style={{ position: 'absolute', top: '-4', right: '-1' }}
                                title="You are following this actor" />
                        }
                    </Link>
                </div>
            )
            }
        </div>
    );
}