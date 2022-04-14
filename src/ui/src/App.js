import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import { ActorSearchPage } from './pages/ActorSearchPage';
import { ActorDetailPage } from './pages/ActorDetailPage';
import { MyActorAlertsPage } from './pages/MyActorAlertsPage';
import { LoginCard } from './components/LoginCard';

import './App.scss';

function App() {

  return (
    <div className="App">
      <Router>
        <div className="app-header-section">
          <div className="app-header-name">
            <h1><Link className="app-header-name-link" to="/actors/search/">Actor Alerts</Link></h1>
          </div>
          <div className="app-header-login">
            <LoginCard />
          </div>
        </div>

        <Routes>
          <Route path='/actors/search/' element={<ActorSearchPage />} />
          <Route path='/actors/:actorId' element={<ActorDetailPage />} />
          <Route path='/myactoralerts/' element={<MyActorAlertsPage />} />
          <Route path="*" element={<Navigate to="/actors/search/" replace />} />
        </Routes>
      </Router>
    </div >
  );
}

export default App;
