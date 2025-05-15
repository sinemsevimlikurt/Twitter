import React, { useState, useEffect } from 'react';
import './CreateTweet.css';

const CreateTweet = ({ onTweetCreated }) => {
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);

  // Fetch current user on component mount
  useEffect(() => {
    const fetchCurrentUser = async () => {
      try {
        const response = await fetch('/api/auth/current-user', {
          credentials: 'include'
        });
        
        if (response.ok) {
          const userData = await response.json();
          setCurrentUser(userData);
        } else if (response.status === 401) {
          // User is not authenticated, which is fine
          console.log('User is not authenticated');
          setCurrentUser(null);
        } else {
          console.error('Error fetching current user:', response.status);
          setCurrentUser(null);
        }
      } catch (err) {
        console.error('Error fetching current user:', err);
        setCurrentUser(null);
      }
    };
    
    fetchCurrentUser();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content.trim()) return;

    // Check if user is authenticated
    if (!currentUser) {
      setError('You must be logged in to create a tweet. Please log in first.');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await fetch('/api/tweet', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ 
          content,
          tweetId: null,  // The backend will generate this
          userId: null,   // The backend will get this from the authenticated user
          username: null  // The backend will get this from the authenticated user
        }),
        credentials: 'include', // Important for cookies
      });

      if (!response.ok) {
        throw new Error(`Failed to create tweet: ${response.status}`);
      }

      const newTweet = await response.json();
      setContent('');
      console.log('Successfully created tweet with backend');
      
      // Notify parent component that a new tweet was created
      if (onTweetCreated) {
        onTweetCreated(newTweet);
      }
    } catch (err) {
      console.error('Error creating tweet:', err);
      setError(`Failed to create tweet: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="create-tweet-container">
      <h3>Create a New Tweet</h3>
      {currentUser ? (
        <div className="user-info">
          <span>Tweeting as <strong>@{currentUser.username}</strong></span>
        </div>
      ) : (
        <div className="warning-message">
          <p>You need to be logged in to create tweets.</p>
        </div>
      )}
      <form onSubmit={handleSubmit} className="create-tweet-form">
        {error && <div className="error-message">{error}</div>}
        
        <div className="form-group">
          <textarea
            placeholder="What's happening?"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            maxLength={280}
            required
            disabled={!currentUser}
          />
          <div className="character-count">{content.length}/280</div>
        </div>
        
        <button type="submit" disabled={loading || !content.trim() || !currentUser}>
          {loading ? 'Posting...' : 'Tweet'}
        </button>
      </form>
    </div>
  );
};

export default CreateTweet;
