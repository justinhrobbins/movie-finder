import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, NavLink } from 'react-router-dom';
import { ActorSearchPage } from './pages/ActorSearchPage';
import { ActorDetailPage } from './pages/ActorDetailPage';
import { ActorAlertsPage } from './pages/ActorAlertsPage';
import { LoginCard } from './components/LoginCard';
import { UserContext } from "./UserContext";

import './App.scss';

function App() {
  const [user, setUserContext] = useState(null);

  return (
    <div className="App">
      <UserContext.Provider value={{ user, setUserContext }}>
        <Router>
          <div className="app-header-section">
            <div className="app-header-name">
              <h1>Actor Alerts</h1>
            </div>
            <div className="app-header-login">
              <LoginCard />
            </div>
            <div className="app-header-menu">
              <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to="/actors/search/">Home</NavLink>
              <NavLink className={({ isActive }) => (isActive ? 'app-header-menu-link-active' : 'app-header-menu-link-inactive')} to='/actoralerts/'>My Actor Alerts</NavLink>
            </div>
          </div>

          <Routes>
            <Route path='/actors/search/' element={<ActorSearchPage />} />
            <Route path='/actors/:actorId' element={<ActorDetailPage />} />
            <Route path='/actoralerts/' element={<ActorAlertsPage />} />

            <Route path="*" element={<Navigate to="/actors/search/" replace />} />
          </Routes>
        </Router>
      </UserContext.Provider>
    </div >
  );
}

export default App;
