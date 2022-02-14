import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ActorSearchPage } from './pages/ActorSearchPage';

function App() {
  return (
    <div className="App">
      <h1>Movie Finder</h1>
      <Router>
        <Routes>
          <Route path='/actors/:actorName' element={<ActorSearchPage/>} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
