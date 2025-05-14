import React, { useState, useEffect } from 'react';
import './TweetList.css';

const TweetList = () => {
  const [tweets, setTweets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userId, setUserId] = useState(1); // Default user ID

  useEffect(() => {
    fetchTweets();
  }, [userId]);

  const fetchTweets = async () => {
    setLoading(true);
    try {
      // Using relative URL with proxy configuration to avoid CORS issues
      const response = await fetch(`/api/tweet/findByUserId?userId=${userId}`);
      
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      
      // Check if the response is JSON or HTML
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        const data = await response.json();
        setTweets(data);
        setError(null);
      } else {
        // If we got HTML instead of JSON, it's likely a login page or error page
        const text = await response.text();
        console.error('Received HTML instead of JSON:', text.substring(0, 200));
        throw new Error('Received HTML instead of JSON. You might need to authenticate first.');
      }
    } catch (err) {
      console.error('Error fetching tweets:', err);
      setError(`Failed to fetch tweets: ${err.message}`);
      // For demonstration purposes, let's add some mock data
      setTweets([
        { tweetId: 1, content: 'This is a mock tweet since we got an error', username: 'user1' },
        { tweetId: 2, content: 'The backend might require authentication', username: 'user1' }
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleUserIdChange = (e) => {
    const newUserId = parseInt(e.target.value, 10) || 1;
    setUserId(newUserId);
  };

  return (
    <div className="tweet-list-container">
      <h2>User Tweets</h2>
      
      <div className="user-input">
        <label htmlFor="userId">User ID:</label>
        <input 
          type="number" 
          id="userId" 
          value={userId} 
          onChange={handleUserIdChange} 
          min="1"
        />
        <button onClick={fetchTweets}>Fetch Tweets</button>
      </div>

      {error && (
        <div className="error-message">
          <p>{error}</p>
          {error.includes('HTML instead of JSON') ? (
            <div>
              <p>This error occurs because the backend is returning HTML instead of JSON.</p>
              <p>Possible reasons:</p>
              <ul>
                <li>You need to authenticate first (login to the backend)</li>
                <li>The backend server is redirecting to a login page</li>
                <li>The Spring Security configuration is intercepting the request</li>
              </ul>
              <p>Solutions:</p>
              <ul>
                <li>Implement a login form in the frontend</li>
                <li>Modify the Spring Security configuration to allow unauthenticated access to the API endpoints</li>
              </ul>
            </div>
          ) : error.includes('CORS') ? (
            <div>
              <p>This is a CORS error. Check the console for details.</p>
              <p>The solution is to add CORS configuration to the Spring Boot backend.</p>
            </div>
          ) : (
            <p>Check the console for more details about this error.</p>
          )}
        </div>
      )}

      {loading ? (
        <p>Loading tweets...</p>
      ) : (
        <div className="tweets">
          {tweets.length === 0 ? (
            <p>No tweets found for this user.</p>
          ) : (
            tweets.map((tweet) => (
              <div key={tweet.tweetId} className="tweet">
                <h3>@{tweet.username}</h3>
                <p>{tweet.content}</p>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default TweetList;
