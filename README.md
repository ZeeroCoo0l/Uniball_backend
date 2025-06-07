# UniBall - Spring Boot Backend

UniBall is a mobile application designed for university football players and coaches to organize training sessions, manage team communication, and track player engagement. This repository contains the Spring Boot backend that powers the UniBall mobile app.

## 📱 Project Overview

UniBall addresses common challenges in student football teams:
- Poor communication through scattered WhatsApp groups
- Last-minute training announcements
- Lack of structure and training variation
- Difficulty tracking attendance and engagement

The app provides structured scheduling, team management, gamification through voting/awards, and training exercise suggestions.

## 🏗️ Architecture

The backend follows a microservice-ready architecture using Spring Boot with MariaDB for data persistence and Supabase for authentication and file storage.

### Technology Stack
- **Framework:** Spring Boot
- **Database:** MariaDB
- **Authentication:** Supabase (JWT-based)
- **File Storage:** Supabase Storage
- **Testing:** JUnit, Postman
- **CI/CD:** Jenkins
- **Server:** Tomcat

### Authentication Flow
1. User registration/login handled by Supabase
2. Supabase generates JWT tokens with user session data
3. Spring Boot validates JWT tokens using Supabase JWT key
4. User data synchronized between Supabase and MariaDB for consistency

## 📊 Data Model

### Core Entities

- **UserClient**: User profiles with positions, profile pictures, description, name and team memberships, 
- **Team**: Football teams with members and admin relationships
- **Event**: Base class for team activities
  - **Practice**: Subclass for training sessions with specific details
- **Award**: Player recognition system (MVP, Best Goal, Best Assist)
- **Exercise**: Training exercise database for coaches

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MariaDB 10.5+
- Supabase account and project

## 🔧 API Endpoints

### Authentication
All endpoints require JWT token in Authorization header:
```
Authorization: Bearer <jwt_token>
```

## 🧪 Testing

### Running Tests
```bash
# Run all unit tests
mvn test
```

### Test Coverage
- **Entity Tests**: Complete unit test coverage for all entities (UserClient, Event, Practice, Team, Award, Exercise)

## 🔒 Security

### JWT Token Validation
```java
// JWT tokens are validated against Supabase
// User session verification ensures secure API access
// Admin endpoints require additional privilege checks
```

## 🚀 Deployment

### Environment Configuration
Set production environment variables for:
- Database connection strings
- Supabase project credentials
- JWT secret keys

## 🔮 Future Development

### Planned Integrations
- **Google Calendar API**: Sync training schedules with personal calendars
- **OpenWeather API**: Weather information for outdoor training sessions
- **AI Integration**: Automated training plan generation based on team needs

### Microservice Migration
The current monolithic structure is designed for easy migration to microservices:
- **User Service**: Authentication and profile management
- **Team Service**: Team operations and memberships
- **Event Service**: Practice and match scheduling
- **Award Service**: Voting and recognition system
- **Exercise Service**: Training content management


## 📄 License

This project is developed as part of a university course at Stockholm University, Department of Computer and Systems Sciences (DSV).

---

**Team:** Group 15:9  
**Course:** PVT15 2025  
**Institution:** Stockholm University - DSV
