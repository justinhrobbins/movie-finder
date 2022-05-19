import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, NavLink } from 'react-router-dom';
import { HomePage } from './pages/HomePage';
import { ActorDetailPage } from './pages/ActorDetailPage';
import { ActorAlertsPage } from './pages/ActorAlertsPage';
import { MyMoviesPage } from './pages/MyMoviesPage';
import { ActorSearchCard } from './components/ActorSearchCard';
// import { LoginCard } from './components/LoginCard';
import { NewLoginCard } from './components/NewLoginCard';
import { UserContext } from "./UserContext";
import { GoogleOAuthProvider } from '@react-oauth/google';

import './App.scss';

function App() {
  const [loggedInUser, setLoggedInUserContext] = useState(JSON.parse(localStorage.getItem('loginData')));

  return (
    <GoogleOAuthProvider clientId="138366908317-902o2sbo5lfsmph1p3qeh4bqkddbr672.apps.googleusercontent.com">
      <div className="App">
        <UserContext.Provider value={{ loggedInUser, setLoggedInUserContext }}>
          <Router>
            <div className="app-header-section">
              <div className="app-header-name">
                <h2>Actor Alerts</h2>
              </div>
              <div className="app-header-login">
                <NewLoginCard />
              </div>
              <div className="app-header-menu">
                <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to="/">Home</NavLink>
                <span>|</span>
                <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to='/actoralerts/'>My Actors</NavLink>
                <span>|</span>
                <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to='/mymovies?filter=recent'>My Movies</NavLink>
              </div>
              <div className="app-header-search-bar">
                <ActorSearchCard />
              </div>
            </div>

            <Routes>
              <Route path='/' element={<HomePage />} />
              <Route path='/actors/:actorId' element={<ActorDetailPage />} />
              <Route path='/actoralerts/' element={<ActorAlertsPage />} />
              <Route path='/mymovies/' element={<MyMoviesPage />} />

              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </Router>
        </UserContext.Provider>
      </div >
    </GoogleOAuthProvider>
  );
}

export default App;
