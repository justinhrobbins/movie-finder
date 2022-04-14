import { React, useEffect, useState } from 'react';

import './ActorDetailCard.scss';

export const ActorDetailCard = ({ actor }) => {
    if (!actor) return null;

    const baseUrl = "https://image.tmdb.org/t/p/w185/";
    const defaultActorUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const actorUrl = actor.profile_path === "" ? defaultActorUrl : baseUrl + actor.profile_path;

    return (
        <div className="ActorDetailCard">
            <div className="actor-detail-card-image-container">
                <img className="actor-detail-card-image-container-image" src={actorUrl} alt={actor.name} title={actor.name} />
                <div className="actor-detail-card-image-container-details">Add to my Actor Alerts</div>
            </div>
            <div className="actor-detail-card-content-section">
                <div className="actor-detail-card-content-section-actor-name">{actor.name}</div>
                <div><span className="actor-detail-card-content-section-label">Birthday:</span> {actor.birthday}</div>
                {
                    (actor && actor.deathday && actor.deathday.length > 0)
                    ? <div><span className="actor-detail-card-content-section-label">Day of Death:</span> {actor.deathday}</div>
                    : <div><span className="actor-detail-card-content-section-label"></span></div>
                }
                <div><span className="actor-detail-card-content-section-label">Place of birth:</span> {actor.place_of_birth}</div>
            </div>
        </div>
    );
}