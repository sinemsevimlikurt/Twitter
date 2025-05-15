import React, { useState } from 'react';
import './Login.css';

const Login = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Authenticate using the login endpoint
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
        credentials: 'include', // Important for cookies
      });

      // Check for successful login
      if (!response.ok) {
        // If the backend returns an error, throw an error
        throw new Error(`Login failed: ${response.status}`);
      }
      
      console.log('Successfully logged in with backend authentication');
      
      // If we get here, the backend is available, continue with normal flow
      // Parse the response data
      const authData = await response.json();
      
      if (authData.status === 'error') {
        throw new Error('Invalid username or password');
      }
      
      // Wait a short time for the session to be established
      await new Promise(resolve => setTimeout(resolve, 500));
      
      // If login is successful, fetch the current user to verify
      const userResponse = await fetch('/api/auth/current-user', {
        credentials: 'include' // Important for cookies
      });

      if (userResponse.ok) {
        // Successfully authenticated
        console.log('Successfully authenticated and verified user');
        // Get the user data for the profile
        const userData = await userResponse.json();
        // Call onLoginSuccess with the user data
        onLoginSuccess(userData);
      } else {
        console.error('Failed to verify user after login:', userResponse.status);
        throw new Error('Login succeeded but session was not established properly');
      }
    } catch (err) {
      console.error('Login error:', err);
      setError(`${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <h2>Login to Twitter API</h2>
      <form onSubmit={handleSubmit} className="login-form">
        {error && <div className="error-message">{error}</div>}
        
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        
        <button type="submit" disabled={loading}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>
      
      <div className="login-help">
        <p>This login form connects to the Spring Boot backend's authentication system.</p>
        <p>After successful login, you'll be able to fetch tweets from the API.</p>
        <p><strong>Note:</strong> If you don't have login credentials, you may need to modify the Spring Security configuration to allow unauthenticated access to the API endpoints.</p>
      </div>
    </div>
  );
};

export default Login;
