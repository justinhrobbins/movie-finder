import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ActorSearchPage } from './pages/ActorSearchPage';
import { ActorDetailPage } from './pages/ActorDetailPage';
import { LoginCard } from './components/LoginCard';

function App() {

  return (
    <div className="App">
      <h1>Actor Alerts</h1>

      <LoginCard />

      <Router>
        <Routes>
          <Route path='/actors/search/' element={<ActorSearchPage />} />
          <Route path='/actors/:actorId' element={<ActorDetailPage />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
