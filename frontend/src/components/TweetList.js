import React, { useState, useEffect } from 'react';
import './TweetList.css';
import CreateTweet from './CreateTweet';
import UserProfile from './UserProfile';
import SearchTweets from './SearchTweets';

const TweetList = ({ currentUser: initialCurrentUser }) => {
  const [tweets, setTweets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [actionLoading, setActionLoading] = useState(false);
  const [currentUser, setCurrentUser] = useState(initialCurrentUser);
  const [clickedUser, setClickedUser] = useState(null);
  const [view, setView] = useState('feed'); // 'feed', 'profile', 'search', or 'userProfile'

  useEffect(() => {
    // Only fetch current user if not already provided via props
    if (!initialCurrentUser) {
      fetchCurrentUser();
    }
    fetchAllTweets();
  }, [initialCurrentUser]);

  const fetchCurrentUser = async () => {
    try {
      const response = await fetch('/api/auth/current-user', {
        credentials: 'include'
      });
      
      if (response.ok) {
        const userData = await response.json();
        setCurrentUser(userData);
      } else {
        // If the backend is not available, use mock user data
        console.log('Using mock user data since backend is not available');
        const mockUser = {
          userId: 1,
          username: 'currentUser',
          email: 'user@example.com'
        };
        setCurrentUser(mockUser);
      }
    } catch (err) {
      console.error('Error fetching current user:', err);
      // If there's an error, use mock user data
      const mockUser = {
        userId: 1,
        username: 'currentUser',
        email: 'user@example.com'
      };
      setCurrentUser(mockUser);
    }
  };

  const fetchAllTweets = async () => {
    setLoading(true);
    try {
      // Fetch all tweets instead of just for a specific user
      const response = await fetch('/api/tweet/all', {
        credentials: 'include' // Important for cookies
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      
      // Check if the response is JSON or HTML
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        const data = await response.json();
        setTweets(data);
        setError(null);
        console.log('Successfully fetched tweets from backend');
      } else {
        // If we got HTML instead of JSON, it's likely a login page or error page
        const text = await response.text();
        console.error('Received HTML instead of JSON:', text.substring(0, 200));
        throw new Error('Received HTML instead of JSON. You might need to authenticate first.');
      }
    } catch (err) {
      console.error('Error fetching tweets:', err);
      setError(`Failed to fetch tweets: ${err.message}`);
      setTweets([]);
    } finally {
      setLoading(false);
    }
  };

  const handleLike = async (tweetId) => {
    setActionLoading(true);
    try {
      const response = await fetch(`/api/like`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        // The LikeController expects a LikeDTO with tweetId field
        body: JSON.stringify({ tweetId: tweetId, userId: null, username: null }),
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Failed to like tweet: ${response.status}`);
      }
      
      // Refresh tweets to get accurate state from server
      fetchAllTweets();
    } catch (err) {
      console.error('Error liking tweet:', err);
      setError(`Failed to like tweet: ${err.message}`);
    } finally {
      setActionLoading(false);
    }
  };

  const handleRetweet = async (tweetId) => {
    setActionLoading(true);
    try {
      // Send tweetId as a query parameter instead of in the request body
      const response = await fetch(`/api/retweet?tweetId=${tweetId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        // No body needed as we're using query parameters
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Failed to retweet: ${response.status}`);
      }
      
      // Refresh tweets to get accurate state from server
      fetchAllTweets();
    } catch (err) {
      console.error('Error retweeting:', err);
      setError(`Failed to retweet: ${err.message}`);
    } finally {
      setActionLoading(false);
    }
  };

  const handleDelete = async (tweetId) => {
    if (!window.confirm('Bu tweeti silmek istediğinizden emin misiniz?')) {
      return;
    }

    setActionLoading(true);
    try {
      // Make the API call to delete the tweet
      const response = await fetch(`/api/tweet/${tweetId}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Tweet silme başarısız: ${response.status}`);
      }

      // Refresh tweets to get accurate state from server
      fetchAllTweets();
      console.log('Tweet başarıyla silindi');
    } catch (err) {
      console.error('Tweet silme hatası:', err);
      setError(`Tweet silinemedi: ${err.message}`);
    } finally {
      setActionLoading(false);
    }
  };

  const handleTweetCreated = (newTweet) => {
    // Refresh the tweets list after creating a new tweet
    fetchAllTweets();
  };

  const navigateToProfile = () => {
    setView('profile');
  };

  const navigateToSearch = () => {
    setView('search');
  };

  const navigateToFeed = () => {
    setView('feed');
  };

  const handleUsernameClick = (userId, username) => {
    // Set the clicked user in state and navigate to their profile
    setClickedUser({ userId, username });
    setView('userProfile');
  };

  // Function to format relative time (e.g., '2 minutes ago', '1 hour ago')
  const formatRelativeTime = (timestamp) => {
    if (!timestamp) return 'Zaman bilgisi yok';
    
    // Make sure timestamp is a Date object
    const now = new Date();
    let createdAt;
    
    try {
      // If timestamp is already a Date object, this will work
      // If it's a string, it will be converted to a Date
      createdAt = new Date(timestamp);
      
      // Check if the date is valid
      if (isNaN(createdAt.getTime())) {
        return 'Geçersiz tarih';
      }
    } catch (error) {
      console.error('Error parsing date:', error);
      return 'Tarih hatası';
    }
    
    const diffInSeconds = Math.floor((now - createdAt) / 1000);
    
    if (diffInSeconds < 60) {
      return `${diffInSeconds} saniye önce`;
    }
    
    const diffInMinutes = Math.floor(diffInSeconds / 60);
    if (diffInMinutes < 60) {
      return `${diffInMinutes} dakika önce`;
    }
    
    const diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) {
      return `${diffInHours} saat önce`;
    }
    
    const diffInDays = Math.floor(diffInHours / 24);
    if (diffInDays < 30) {
      return `${diffInDays} gün önce`;
    }
    
    const diffInMonths = Math.floor(diffInDays / 30);
    if (diffInMonths < 12) {
      return `${diffInMonths} ay önce`;
    }
    
    const diffInYears = Math.floor(diffInMonths / 12);
    return `${diffInYears} yıl önce`;
  };

  return (
    <div className="tweet-list-container">
      {view === 'feed' && (
        <>
          <div className="feed-header">
            <h2>Twitter Feed</h2>
            <div className="navigation-buttons">
              {currentUser && (
                <button className="nav-button profile-button" onClick={navigateToProfile}>
                  My Profile
                </button>
              )}
              <button className="nav-button search-button" onClick={navigateToSearch}>
                Search Users
              </button>
            </div>
          </div>
          
          <CreateTweet onTweetCreated={handleTweetCreated} />
          
          {error && (
            <div className="error-message">
              <p>{error}</p>
              <div>
                <p>This application requires a connection to the backend server to function properly.</p>
                <p>Please ensure that:</p>
                <ul>
                  <li>The backend server is running on port 3456</li>
                  <li>You are properly authenticated if required</li>
                  <li>The database connection is configured correctly</li>
                </ul>
                <p>Check the console for more detailed error information.</p>
              </div>
            </div>
          )}

          {loading ? (
            <p>Loading tweets...</p>
          ) : (
            <div className="tweets">
              {tweets.length === 0 ? (
                <p>No tweets found.</p>
              ) : (
                tweets.map((tweet) => (
                  <div key={tweet.tweetId} className="tweet" data-tweet-id={tweet.tweetId}>
                    <div className="tweet-header">
                      <h3 className="username" onClick={() => handleUsernameClick(tweet.userId, tweet.username)}>@{tweet.username}</h3>
                      <span className="tweet-time">{formatRelativeTime(tweet.createdAt)}</span>
                    </div>
                    <p>{tweet.content}</p>
                    <div className="tweet-actions">
                      <button 
                        onClick={() => handleLike(tweet.tweetId)} 
                        disabled={actionLoading}
                        className="action-button like-button"
                      >
                        ❤️ {tweet.likeCount || 0}
                      </button>
                      <button 
                        onClick={() => handleRetweet(tweet.tweetId)} 
                        disabled={actionLoading}
                        className="action-button retweet-button"
                      >
                        🔄 {tweet.retweetCount || 0}
                      </button>
                      {currentUser && tweet.userId === currentUser.userId && (
                        <button 
                          onClick={() => handleDelete(tweet.tweetId)} 
                          disabled={actionLoading}
                          className="action-button delete-button"
                        >
                          🗑️ Sil
                        </button>
                      )}
                    </div>
                  </div>
                ))
              )}
            </div>
          )}
        </>
      )}

      {view === 'profile' && currentUser && (
        <UserProfile 
          currentUser={currentUser} 
          onBack={navigateToFeed} 
          onUsernameClick={handleUsernameClick}
        />
      )}

      {view === 'userProfile' && clickedUser && (
        <UserProfile 
          currentUser={clickedUser} 
          onBack={navigateToFeed} 
          isOtherUser={true} 
          onUsernameClick={handleUsernameClick}
        />
      )}

      {view === 'search' && (
        <SearchTweets onBack={navigateToFeed} onUsernameClick={handleUsernameClick} />
      )}
    </div>
  );
};

export default TweetList;
