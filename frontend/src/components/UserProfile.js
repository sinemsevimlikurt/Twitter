import React, { useState, useEffect } from 'react';
import './UserProfile.css';

const UserProfile = ({ currentUser, onBack, isOtherUser = false, onUsernameClick }) => {
  const [activeTab, setActiveTab] = useState('tweets'); // 'tweets' or 'likes'
  const [tweets, setTweets] = useState([]);
  const [likes, setLikes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (currentUser) {
      if (activeTab === 'tweets') {
        fetchUserTweets();
      } else if (activeTab === 'likes') {
        fetchUserLikes();
      }
    }
  }, [currentUser, activeTab]);

  const fetchUserTweets = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`/api/tweet/findByUserId?userId=${currentUser.userId}`, {
        credentials: 'include'
      });

      if (!response.ok) {
        throw new Error(`Failed to fetch tweets: ${response.status}`);
      }

      const data = await response.json();
      setTweets(data);
    } catch (err) {
      console.error('Error fetching user tweets:', err);
      setError(`Failed to fetch tweets: ${err.message}`);
      
      // Function to generate random past timestamps (between 1 minute and 7 days ago)
      const generateRandomPastTime = () => {
        const now = new Date();
        const randomMinutesAgo = Math.floor(Math.random() * 10080) + 1; // Random minutes between 1 and 7 days (10080 minutes)
        return new Date(now.getTime() - randomMinutesAgo * 60000);
      };
      
      // Use mock data when backend is not available
      const mockTweets = [
        { 
          tweetId: 6, 
          content: 'This is a tweet from the current user profile', 
          username: currentUser.username,
          userId: currentUser.userId,
          likeCount: 3,
          retweetCount: 1,
          createdAt: generateRandomPastTime()
        },
        { 
          tweetId: 7, 
          content: 'Another tweet from the current user profile', 
          username: currentUser.username,
          userId: currentUser.userId,
          likeCount: 5,
          retweetCount: 2,
          createdAt: generateRandomPastTime()
        }
      ];
      setTweets(mockTweets);
    } finally {
      setLoading(false);
    }
  };

  const fetchUserLikes = async () => {
    setLoading(true);
    setError(null);
    try {
      // Assuming there's an endpoint to get tweets liked by a user
      const response = await fetch(`/api/like/user/${currentUser.userId}`, {
        credentials: 'include'
      });

      if (!response.ok) {
        throw new Error(`Failed to fetch likes: ${response.status}`);
      }

      const data = await response.json();
      setLikes(data);
    } catch (err) {
      console.error('Error fetching user likes:', err);
      setError(`Failed to fetch likes: ${err.message}`);
      
      // Function to generate random past timestamps (between 1 minute and 7 days ago)
      const generateRandomPastTime = () => {
        const now = new Date();
        const randomMinutesAgo = Math.floor(Math.random() * 10080) + 1; // Random minutes between 1 and 7 days (10080 minutes)
        return new Date(now.getTime() - randomMinutesAgo * 60000);
      };
      
      // Use mock data when backend is not available
      const mockLikes = [
        { 
          tweetId: 1, 
          content: 'Welcome to our Twitter clone! This is a mock tweet since the backend is not available.', 
          username: 'admin',
          userId: 1,
          likeCount: 15,
          retweetCount: 5,
          createdAt: generateRandomPastTime()
        },
        { 
          tweetId: 3, 
          content: 'You can like and retweet posts. The count will update immediately in the UI.', 
          username: 'user1',
          userId: 3,
          likeCount: 7,
          retweetCount: 2,
          createdAt: generateRandomPastTime()
        }
      ];
      setLikes(mockLikes);
    } finally {
      setLoading(false);
    }
  };

  const handleLike = async (tweetId) => {
    // First, update the UI optimistically
    if (activeTab === 'tweets') {
      setTweets(prevTweets => {
        return prevTweets.map(tweet => {
          if (tweet.tweetId === tweetId) {
            // Check if the tweet is already liked by looking at the UI state
            const isLiked = document.querySelector(`[data-tweet-id="${tweetId}"] .like-button`).classList.contains('active');
            
            // Toggle the like count based on current state
            const newLikeCount = isLiked ? (tweet.likeCount - 1) : (tweet.likeCount + 1);
            return { ...tweet, likeCount: newLikeCount };
          }
          return tweet;
        });
      });
    } else if (activeTab === 'likes') {
      setLikes(prevLikes => {
        return prevLikes.map(like => {
          if (like.tweetId === tweetId) {
            // Check if the tweet is already liked by looking at the UI state
            const isLiked = document.querySelector(`[data-tweet-id="${tweetId}"] .like-button`).classList.contains('active');
            
            // Toggle the like count based on current state
            const newLikeCount = isLiked ? (like.likeCount - 1) : (like.likeCount + 1);
            return { ...like, likeCount: newLikeCount };
          }
          return like;
        });
      });
    }

    // Then make the API call
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

      // No need to refresh the data since we've already updated the UI
    } catch (err) {
      console.error('Error liking tweet:', err);
      // If there's an error, we should revert the optimistic update
      // but for simplicity in this demo, we'll just show an alert
      alert(`Failed to like tweet: ${err.message}`);
    }
  };

  const handleRetweet = async (tweetId) => {
    // First, update the UI optimistically
    if (activeTab === 'tweets') {
      setTweets(prevTweets => {
        return prevTweets.map(tweet => {
          if (tweet.tweetId === tweetId) {
            // Check if the tweet is already retweeted by looking at the UI state
            const isRetweeted = document.querySelector(`[data-tweet-id="${tweetId}"] .retweet-button`).classList.contains('active');
            
            // Toggle the retweet count based on current state
            const newRetweetCount = isRetweeted ? (tweet.retweetCount - 1) : (tweet.retweetCount + 1);
            return { ...tweet, retweetCount: newRetweetCount };
          }
          return tweet;
        });
      });
    } else if (activeTab === 'likes') {
      setLikes(prevLikes => {
        return prevLikes.map(like => {
          if (like.tweetId === tweetId) {
            // Check if the tweet is already retweeted by looking at the UI state
            const isRetweeted = document.querySelector(`[data-tweet-id="${tweetId}"] .retweet-button`).classList.contains('active');
            
            // Toggle the retweet count based on current state
            const newRetweetCount = isRetweeted ? (like.retweetCount - 1) : (like.retweetCount + 1);
            return { ...like, retweetCount: newRetweetCount };
          }
          return like;
        });
      });
    }

    // Then make the API call
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

      // No need to refresh the data since we've already updated the UI
    } catch (err) {
      console.error('Error retweeting:', err);
      // If there's an error, we should revert the optimistic update
      // but for simplicity in this demo, we'll just show an alert
      alert(`Failed to retweet: ${err.message}`);
    }
  };

  const handleDelete = async (tweetId) => {
    if (!window.confirm('Bu tweeti silmek istediğinizden emin misiniz?')) {
      return;
    }

    try {
      // Optimistically update the UI first
      if (activeTab === 'tweets') {
        setTweets(prevTweets => prevTweets.filter(tweet => tweet.tweetId !== tweetId));
      } else if (activeTab === 'likes') {
        // For likes tab, we might want to keep the tweet in the list but mark it as deleted
        // or we can remove it from the likes list as well
        setLikes(prevLikes => prevLikes.filter(like => like.tweetId !== tweetId));
      }
      
      // Make the API call to delete the tweet
      const response = await fetch(`/api/tweet/${tweetId}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Tweet silme başarısız: ${response.status}`);
      }

      // No need to refresh since we've already updated the UI
      console.log('Tweet başarıyla silindi');
    } catch (err) {
      console.error('Tweet silme hatası:', err);
      alert(`Tweet silinemedi: ${err.message}`);
      // Revert the optimistic update on error by refreshing the data
      if (activeTab === 'tweets') {
        fetchUserTweets();
      } else if (activeTab === 'likes') {
        fetchUserLikes();
      }
    }
  };
  
  const handleUsernameClick = (userId, username) => {
    // If we're viewing our own profile, do nothing
    if (!isOtherUser && currentUser.userId === userId) {
      return;
    }
    
    // Otherwise, navigate to the clicked user's profile
    // We'll need to pass this up to the parent component
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

  return (
    <div className="user-profile-container">
      <div className="profile-header">
        <button className="back-button" onClick={onBack}>
          &larr; Back to Feed
        </button>
        <h2>
          {isOtherUser ? 
            `@${currentUser?.username}'s Profile` : 
            'My Profile'
          }
        </h2>
      </div>

      <div className="profile-tabs">
        <button
          className={`tab-button ${activeTab === 'tweets' ? 'active' : ''}`}
          onClick={() => setActiveTab('tweets')}
        >
          Tweets & Retweets
        </button>
        <button
          className={`tab-button ${activeTab === 'likes' ? 'active' : ''}`}
          onClick={() => setActiveTab('likes')}
        >
          Likes
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <p className="loading-message">Loading...</p>
      ) : (
        <div className="tweets-container">
          {activeTab === 'tweets' && (
            <>
              <h3>Tweets & Retweets</h3>
              {tweets.length === 0 ? (
                <p className="no-content-message">No tweets or retweets yet.</p>
              ) : (
                tweets.map((tweet) => (
                  <div key={tweet.tweetId} className="tweet" data-tweet-id={tweet.tweetId}>
                    <div className="tweet-header">
                      <h4 className="username" onClick={() => handleUsernameClick(tweet.userId, tweet.username)}>@{tweet.username}</h4>
                      <span className="tweet-time">{formatRelativeTime(tweet.createdAt)}</span>
                    </div>
                    <p>{tweet.content}</p>
                    <div className="tweet-actions">
                      <button
                        onClick={() => handleLike(tweet.tweetId)}
                        className={`action-button like-button ${tweet.isLikedByCurrentUser ? 'active' : ''}`}
                      >
                        ❤️ {tweet.likeCount || 0}
                      </button>
                      <button
                        onClick={() => handleRetweet(tweet.tweetId)}
                        className={`action-button retweet-button ${tweet.isRetweetedByCurrentUser ? 'active' : ''}`}
                      >
                        🔄 {tweet.retweetCount || 0}
                      </button>
                      {!isOtherUser && (
                        <button
                          onClick={() => handleDelete(tweet.tweetId)}
                          className="action-button delete-button"
                        >
                          🗑️ Sil
                        </button>
                      )}
                    </div>
                  </div>
                ))
              )}
            </>
          )}

          {activeTab === 'likes' && (
            <>
              <h3 className="liked-section-header">
                <span>❤️</span> Liked Tweets
              </h3>
              {likes.length === 0 ? (
                <p className="no-content-message">No liked tweets yet.</p>
              ) : (
                likes.map((like) => (
                  <div key={like.tweetId} className="tweet liked-tweet" data-tweet-id={like.tweetId}>
                    <div className="tweet-header">
                      <h4 className="username" onClick={() => handleUsernameClick(like.userId, like.username)}>@{like.username}</h4>
                      <span className="tweet-time">{formatRelativeTime(like.createdAt)}</span>
                    </div>
                    <p>{like.content}</p>
                    <div className="tweet-actions">
                      <button
                        onClick={() => handleLike(like.tweetId)}
                        className="action-button like-button active"
                      >
                        ❤️ {like.likeCount || 0}
                      </button>
                      <button
                        onClick={() => handleRetweet(like.tweetId)}
                        className={`action-button retweet-button ${like.isRetweetedByCurrentUser ? 'active' : ''}`}
                      >
                        🔄 {like.retweetCount || 0}
                      </button>
                    </div>
                  </div>
                ))
              )}
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default UserProfile;
