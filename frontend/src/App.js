import React, { useState } from 'react';
import './App.css';
import TweetList from './components/TweetList';
import Login from './components/Login';
import Register from './components/Register';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [activeTab, setActiveTab] = useState('login'); // 'login' or 'register'

  const handleLoginSuccess = () => {
    setIsAuthenticated(true);
  };

  const handleRegisterSuccess = () => {
    // Switch to login tab after successful registration
    setActiveTab('login');
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Twitter API Frontend</h1>
        <p>This application demonstrates CORS issues and solutions</p>
      </header>
      <main>
        {isAuthenticated ? (
          <TweetList />
        ) : (
          <div className="auth-section">
            <div className="auth-explanation">
              <h2>Authentication Required</h2>
              <p>The Twitter API requires authentication before you can access tweets.</p>
              <p>This is a common scenario in real-world applications and is related to the CORS issue we're exploring.</p>
              <p>When the frontend and backend are on different origins (ports in our case), we need to handle both CORS and authentication.</p>
            </div>
            
            <div className="auth-tabs">
              <button 
                className={`tab-button ${activeTab === 'login' ? 'active' : ''}`}
                onClick={() => setActiveTab('login')}
              >
                Login
              </button>
              <button 
                className={`tab-button ${activeTab === 'register' ? 'active' : ''}`}
                onClick={() => setActiveTab('register')}
              >
                Register
              </button>
            </div>
            
            {activeTab === 'login' ? (
              <Login onLoginSuccess={handleLoginSuccess} />
            ) : (
              <Register onRegisterSuccess={handleRegisterSuccess} />
            )}
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
