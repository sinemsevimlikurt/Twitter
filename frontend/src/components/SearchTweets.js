import React, { useState } from 'react';
import './SearchTweets.css';

const SearchTweets = ({ onBack, onUsernameClick }) => {
  const [username, setUsername] = useState('');
  const [tweets, setTweets] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searched, setSearched] = useState(false);

  const handleUsernameClick = (userId, username) => {
    if (typeof onUsernameClick === 'function') {
      onUsernameClick(userId, username);
    }
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

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!username.trim()) return;

    setLoading(true);
    setError(null);
    setSearched(true);

    try {
      // Assuming there's an endpoint to search tweets by username
      const response = await fetch(`/api/tweet/search?username=${encodeURIComponent(username)}`, {
        credentials: 'include'
      });

      if (!response.ok) {
        throw new Error(`Search failed: ${response.status}`);
      }

      const data = await response.json();
      setTweets(data);
    } catch (err) {
      console.error('Error searching tweets:', err);
      setError(`Search failed: ${err.message}`);
      
      // Function to generate random past timestamps (between 1 minute and 7 days ago)
      const generateRandomPastTime = () => {
        const now = new Date();
        const randomMinutesAgo = Math.floor(Math.random() * 10080) + 1; // Random minutes between 1 and 7 days (10080 minutes)
        return new Date(now.getTime() - randomMinutesAgo * 60000);
      };

      // Generate mock data based on the searched username
      if (username.trim().toLowerCase() === 'user1') {
        const mockTweets = [
          { 
            tweetId: 3, 
            content: 'You can like and retweet posts. The count will update immediately in the UI.', 
            username: 'user1',
            userId: 3,
            likeCount: 7,
            retweetCount: 2,
            createdAt: generateRandomPastTime()
          },
          { 
            tweetId: 5, 
            content: 'This is another tweet from user1 showing in search results!', 
            username: 'user1',
            userId: 3,
            likeCount: 3,
            retweetCount: 1,
            createdAt: generateRandomPastTime()
          }
        ];
        setTweets(mockTweets);
      } else if (username.trim().toLowerCase() === 'user2') {
        const mockTweets = [
          { 
            tweetId: 4, 
            content: 'Check out your profile to see your tweets and likes in one place!', 
            username: 'user2',
            userId: 4,
            likeCount: 5,
            retweetCount: 1,
            createdAt: generateRandomPastTime()
          }
        ];
        setTweets(mockTweets);
      } else if (username.trim().toLowerCase() === 'admin') {
        const mockTweets = [
          { 
            tweetId: 1, 
            content: 'Welcome to our Twitter clone! This is a mock tweet since the backend is not available.', 
            username: 'admin',
            userId: 1,
            likeCount: 15,
            retweetCount: 5,
            createdAt: generateRandomPastTime()
          }
        ];
        setTweets(mockTweets);
      } else {
        // No tweets found for this username
        setTweets([]);
      }
    } finally {
      setLoading(false);
    }
  };

  const handleLike = async (tweetId) => {
    try {
      const response = await fetch(`/api/like`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ tweetId: tweetId, userId: null, username: null }),
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Failed to like tweet: ${response.status}`);
      }

      // Refresh search results
      handleSearch(new Event('submit'));
    } catch (err) {
      console.error('Error liking tweet:', err);
      alert(`Failed to like tweet: ${err.message}`);
    }
  };

  const handleRetweet = async (tweetId) => {
    try {
      const response = await fetch(`/api/retweet?tweetId=${tweetId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Failed to retweet: ${response.status}`);
      }

      // Refresh search results
      handleSearch(new Event('submit'));
    } catch (err) {
      console.error('Error retweeting:', err);
      alert(`Failed to retweet: ${err.message}`);
    }
  };

  return (
    <div className="search-tweets-container">
      <div className="search-header">
        <button className="back-button" onClick={onBack}>
          &larr; Back to Feed
        </button>
        <h2>Search Tweets by Username</h2>
      </div>

      <form onSubmit={handleSearch} className="search-form">
        <input
          type="text"
          placeholder="Enter username..."
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Searching...' : 'Search'}
        </button>
      </form>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <p className="loading-message">Searching...</p>
      ) : (
        <div className="search-results">
          {searched && (
            <h3>
              {tweets.length > 0
                ? `Found ${tweets.length} tweet${tweets.length === 1 ? '' : 's'} by @${username}`
                : `No tweets found for @${username}`}
            </h3>
          )}

          {tweets.map((tweet) => (
            <div key={tweet.tweetId} className="tweet" data-tweet-id={tweet.tweetId}>
              <div className="tweet-header">
                <h4 className="username" onClick={() => onUsernameClick(tweet.userId, tweet.username)}>@{tweet.username}</h4>
                <span className="tweet-time">{formatRelativeTime(tweet.createdAt)}</span>
              </div>
              <p>{tweet.content}</p>
              <div className="tweet-actions">
                <button
                  onClick={() => handleLike(tweet.tweetId)}
                  className="action-button like-button"
                >
                  ❤️ {tweet.likeCount || 0}
                </button>
                <button
                  onClick={() => handleRetweet(tweet.tweetId)}
                  className="action-button retweet-button"
                >
                  🔄 {tweet.retweetCount || 0}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SearchTweets;
