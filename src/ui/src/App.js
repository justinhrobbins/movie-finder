import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, NavLink } from 'react-router-dom';
import { HomePage } from './pages/HomePage';
import { ActorDetailPage } from './pages/ActorDetailPage';
import { MyActorsPage } from './pages/MyActorsPage';
import { MyMoviesPage } from './pages/MyMoviesPage';
import { ActorSearchBox } from './components/ActorSearchBox';
import { GoogleOAuthProvider } from '@react-oauth/google';
import { Login } from './components/Login';
import { UserContext } from "./UserContext";

import './App.scss';

function App() {
  const [loggedInUser, setLoggedInUserContext] = useState(JSON.parse(localStorage.getItem('loginData')));

  return (
    <GoogleOAuthProvider clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}>
      <div className="App">
        <UserContext.Provider value={{ loggedInUser, setLoggedInUserContext }}>
          <Router>
            <div className="app-header-section">
              <div className="app-header-name">
                <h2>Actor Alerts</h2>
              </div>
              <div className="app-header-login">
                <Login />
              </div>
              <div className="app-header-menu">
                <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to="/">Home</NavLink>
                <span>|</span>
                <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to='/myactors?filter=all&sort=popularity'>My Actors</NavLink>
                <span>|</span>
                <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to='/mymovies?filter=recent'>My Movies</NavLink>
              </div>
              <div className="app-header-search-bar">
                <ActorSearchBox />
              </div>
            </div>

            <div className="app-content">
              <Routes>
                <Route path='/' element={<HomePage />} />
                <Route path='/actors/:actorId' element={<ActorDetailPage />} />
                <Route path='/myactors/' element={<MyActorsPage />} />
                <Route path='/mymovies/' element={<MyMoviesPage />} />

                <Route path="*" element={<Navigate to="/" replace />} />
              </Routes>
            </div>

            <div className="app-footer-section">
              <div className="app-footer-tmdb">
                <a href="https://www.themoviedb.org/" target="_blank" rel="noopener noreferrer"><img src="/images/TMDB.svg" width="100" /></a>
                &nbsp;This product uses the TMDB API but is not endorsed or certified by TMDB.
              </div>
              <div className="app-footer-justwatch">
                <a href="https://www.justwatch.com/" target="_blank" rel="noopener noreferrer"><img src="/images/JustWatch.png" width="100" /></a>
                &nbsp;Movie watch provider data is provided by JustWatch
              </div>
            </div>
          </Router>
        </UserContext.Provider>
      </div >
    </GoogleOAuthProvider>
  );
}

export default App;
