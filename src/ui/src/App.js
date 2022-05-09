import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, NavLink } from 'react-router-dom';
import { HomePage } from './pages/HomePage';
import { ActorDetailPage } from './pages/ActorDetailPage';
import { ActorAlertsPage } from './pages/ActorAlertsPage';
import { MyMoviesPage } from './pages/MyMoviesPage';
import { ActorSearchCard } from './components/ActorSearchCard';
import { LoginCard } from './components/LoginCard';
import { UserContext } from "./UserContext";

import './App.scss';

function App() {
  const [loggedInUser, setLoggedInUserContext] = useState(JSON.parse(localStorage.getItem('loginData')));

  return (
    <div className="App">
      <UserContext.Provider value={{ loggedInUser, setLoggedInUserContext }}>
        <Router>
          <div className="app-header-section">
            <div className="app-header-name">
              <h2>Actor Alerts</h2>
            </div>
            <div className="app-header-login">
              <LoginCard />
            </div>
            <div className="app-header-menu">
              <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to="/">Home</NavLink>
              <span>|</span>
              <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to='/actoralerts/'>My Actors</NavLink>
              <span>|</span>
              <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to='/mymovies/'>My Movies</NavLink>
            </div>
            <div className="app-header-search-bar">
              <ActorSearchCard />
            </div>
          </div>

          <Routes>
            <Route path='/' element={<HomePage />} />
            <Route path='/actors/:actorId' element={<ActorDetailPage />} />
            <Route path='/actoralerts/' element={<ActorAlertsPage />} />
            <Route path='/actoralerts/' element={<ActorAlertsPage />} />
            <Route path='/mymovies/' element={<MyMoviesPage />} />

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Router>
      </UserContext.Provider>
    </div >
  );
}

export default App;
