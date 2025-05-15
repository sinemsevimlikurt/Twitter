# Twitter Application

## Overview
This is a Twitter-like application built with Spring Boot for the backend and React for the frontend. The application allows users to create accounts, post tweets, retweet, like tweets, search for tweets by username, and view their profile with their tweets and liked content.

## Features

### Authentication
- User registration and login
- Session-based authentication with cookies
- Logout functionality

### Main Feed
- View all tweets in the main feed
- Create new tweets
- Like and retweet functionality

### User Profile
- View your own tweets and retweets
- View your liked tweets
- Toggle between tweets and likes views

### Search
- Search for tweets by username
- View search results with like and retweet functionality

## Technical Stack

### Backend
- Spring Boot
- Spring Security for authentication
- Spring Data JPA for database access
- H2 Database (for development)

### Frontend
- React
- CSS for styling
- Fetch API for making HTTP requests

## How to Run

### Backend
1. Navigate to the `twitter` directory
2. Run `mvn spring-boot:run` to start the Spring Boot application
3. The backend will be available at http://localhost:3000

### Frontend
1. Navigate to the `frontend` directory
2. Run `npm install` to install dependencies (first time only)
3. Run `npm start` to start the React development server
4. The frontend will be available at http://localhost:3200

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login a user
- `GET /api/auth/current-user` - Get the currently authenticated user
- `POST /api/auth/logout` - Logout the current user

### Tweets
- `GET /api/tweet/all` - Get all tweets
- `GET /api/tweet/findByUserId?userId={userId}` - Get tweets by user ID
- `GET /api/tweet/search?username={username}` - Search tweets by username
- `POST /api/tweet` - Create a new tweet
- `PUT /api/tweet/{tweetId}` - Update a tweet
- `DELETE /api/tweet/{tweetId}` - Delete a tweet

### Likes
- `POST /api/like` - Like a tweet
- `POST /api/like/dislike` - Unlike a tweet
- `GET /api/like/user/{userId}` - Get tweets liked by a user

### Retweets
- `POST /api/retweet?tweetId={tweetId}` - Retweet a tweet

## Notes
- The application uses CORS configuration to allow cross-origin requests between the frontend and backend
- HTTP Basic authentication is used instead of form-based authentication to avoid redirects that interfere with CORS
- Session cookies are used to maintain authentication state

## Future Enhancements
- Add pagination for tweets
- Implement more advanced search capabilities
- Add user profile editing
- Add following/follower functionality
- Implement real-time updates with WebSockets

FSWEB-s19-Challenge
Twitter Api

Hedef:

Bu projenini amacı Spring Boot ile ilgili öğrendiğimiz tüm konuları Pratik etmek amacıyla bir Backend projesi tasarlamaktır. Amacımız Twitter uygulamasını biz yazsaydık nasıl yazardık ? Nelere dikkat ederdik Design ve Implementation kısımlarını nasıl yapardık bunu test etmektir.

Fonksiyonel Zorunluluklar
Proje Spring Boot teknolojisi kullanarak dizayn edilecektir. Veritabanı olarak PostgreSQL kullanılacaktır.

Endpoints:

EASY
http://localhost:3000/tweet[POST] => Tweet oluşturma ve veritabanına kaydetme. Tweet'in hangi kullanıcıya ait olduğu mutlaka tutulmalıdır. Anonym tweetler olmamalıdır.
http://localhost:3000/tweet/findByUserId[GET] => Bir kullanıcının tüm tweetlerini getirmelidir.
http://localhost:3000/tweet/findById[GET] => Bir tweet için tüm bilgilerini getirmelidir.
http://localhost:3000/tweet/:id[PUT] => Bir tweet üzerinde değiştirelecek kısımları update etmek için kullanılmalıdır.
http://localhost:3000/tweet/:id[DELETE] => Id bilgisi verilen tweeti silmek için kullanılır.(Sadece tweet sahibi ilgili tweeti silebilimelidir.)
MEDIUM
http://localhost:3000/comment/[POST] => Bir tweete bir kullanıcı tarafından yorum yazılmasını sağlar.
http://localhost:3000/comment/:id[PUT] => Bir tweete bir kullanıcı tarafından yapılan yorumun update edilmesine olanak sağlar.
http://localhost:3000/comment/:id[DELETE] => Bir tweete bir kullanıcı tarafından yapılan yorumun silinmesini sağlar(Sadece tweet sahibi veya yorum sahibi ilgili yorumu silebilmelidir).
http://localhost:3000/like/[POST] => Bir tweete bir kullanıcı tarafından like atılmasını sağlar.
http://localhost:3000/dislike/[POST] => Bir tweete bir kullanıcı tarafından like atıldıysa bunun silinmesini sağlar.
HARD
http://localhost:3000/retweet/[POST] => Bir tweetin bir kullanıcı tarafından retweet edilmesini sağlar.(Twitter üzerinden retweet özelliğini test ediniz.)
http://localhost:3000/retweet/:id[DELETE] => Retweet edilmiş bir tweetin silinmesi sağlanmalıdır.
Mimari Zorunluluklar
Apimizi hazırlarkan öncelikle tweet, user, comment, like, retweet gibi özellikleri ekleyebilmek adına veritabanımızın nasıl olması gerektiği ile ilgili bir hazırlık yapmalıyız. Veritabanı dizaynı proje için yapmamız gereken ilk adım.
Controller/Service/Repository/Entity katmanlı mimarisi üzerinde sisteminizi dizayn etmelisiniz.
Sisteminiz için tek bir merkezden Global Exception Handling yapmanız beklenmektedir.
Sisteminizde Entity katmanınız üzerinde veritabanınıza gidecek olan fieldlar için validasyon yapmış olmanız beklenmektedir.
Dependency Injection kurallarına uymalısınız.
Yukarda bahsedilen endpointler dışında /register ve /login isminde 2 tane daha endpointiniz olmalı ve security katmanını Spring Security kullanarak yönetmelisiniz.
Projenizde yazılmış fonksiyonları %30'u için Unit Test yazmanız baklenmektedir.
FullStack Developer Muscles:
Twitter Api için bir React ön yüzü oluşturunuz. Bu React ön yüzünün çok detaylı olmasına gerek yoktur. Mesela kullanıcının tüm tweetlerini ekrana basan bir component dizaynı yapılabilir.
Burada amacımız CORS hatası denilen bir problemi gözlemleyip bunun çözümünü tecrübe etmektir. React uygulamanızı 3200 portundan ayağa kaldırınız.
Component'iniz üzerinde kendi yazdığınız endpointlerden biri olan http://localhost:3000/tweet/findByUserId adresine get requesti atınız. Gelen tweetleri ekrana bastırınız.
Karşılaştığınız CORS hatasını nasıl çözersiniz ?