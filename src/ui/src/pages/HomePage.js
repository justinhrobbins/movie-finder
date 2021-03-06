import { React } from 'react';
import { PopularActors } from '../components/PopularActors';

import './scss/HomePage.scss';

export const HomePage = () => {

    return (
        <div className="HomePage">
            <div className="home-page-about-container">
                <div className="home-page-about-intro">
                    Actor Alerts is an 'actor centric' way to find movies you actually want to watch
                    <br /><br />
                    Use Actor Alerts to:
                </div>
                <ul className="home-page-about-list">
                    <li><b>Follow</b> your favorite actors and create your personalized <b>My Actors list</b></li>
                    <li>Quickly find <b>upcoming movies</b> from your favorite actors</li>
                    <li>Quickly find <b>new releases</b> from your favorite actors</li>
                    <li>See which movies from your actors are currently on your <b>streaming services</b></li>
                </ul>
            </div>
            <PopularActors />
        </div>
    );
}