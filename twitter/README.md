# Twitter API

This project is a Twitter-like backend API built with Spring Boot, implementing various features similar to Twitter.

## Project Overview

The goal of this project is to practice Spring Boot concepts by implementing a Twitter-like backend service. The application uses PostgreSQL for data storage and includes features like tweets, comments, likes, and retweets.

## Features

- User authentication with JWT
- Create, read, update, and delete tweets
- Comment on tweets
- Like and unlike tweets
- Retweet functionality

## Technology Stack

- Java 17
- Spring Boot 3.4.5
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Maven

## API Endpoints

### Authentication
- `POST /register` - Register a new user
- `POST /login` - Login and get JWT token

### Tweets
- `POST /tweet` - Create a new tweet
- `GET /tweet/findByUserId` - Get all tweets by a user
- `GET /tweet/findById` - Get a specific tweet by ID
- `PUT /tweet/:id` - Update a tweet
- `DELETE /tweet/:id` - Delete a tweet

### Comments
- `POST /comment` - Add a comment to a tweet
- `PUT /comment/:id` - Update a comment
- `DELETE /comment/:id` - Delete a comment

### Likes
- `POST /like` - Like a tweet
- `POST /dislike` - Unlike a tweet

### Retweets
- `POST /retweet` - Retweet a tweet
- `DELETE /retweet/:id` - Delete a retweet

## Setup and Running

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL

### Database Setup
1. Create a PostgreSQL database named `twitter`
2. Update the database credentials in `application.properties` if needed

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run the following command:
   ```
   mvn spring-boot:run
   ```
4. The application will start on port 3000

## Testing
The project includes unit tests for service layer components. To run the tests:
```
mvn test
```

## Security
The application uses JWT for authentication. All endpoints except `/register` and `/login` require a valid JWT token in the Authorization header.

Example:
```
Authorization: Bearer <your_jwt_token>
```

## Project Structure

- `controller` - REST controllers
- `service` - Business logic
- `repository` - Data access layer
- `entity` - Database entities
- `dto` - Data Transfer Objects
- `security` - JWT and security configuration
- `exception` - Global exception handling
