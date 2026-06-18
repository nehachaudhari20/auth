# Auth

A production-style Authentication and Authorization Service built with Java, Spring Boot, Spring Security, PostgreSQL, JWT, OAuth2, and Flyway.

This project explores how modern applications implement authentication, authorization, token management, OAuth login, session tracking, audit logging, and machine-to-machine authentication.

---

## Features

### Authentication

* User Registration
* User Login
* BCrypt Password Hashing
* JWT Access Tokens
* Refresh Token Architecture
* Google OAuth Login

### Authorization

* Role-Based Access Control (RBAC)
* USER Role
* ADMIN Role
* DEVELOPER Role
* Protected Endpoints

### Session Management

* Session Tracking
* Active Session Listing
* Logout Single Device
* Logout All Devices

### Audit Logging

* User Registration Events
* Login Success Events
* Login Failure Events
* Token Refresh Events
* Session Creation Events
* Session Revocation Events
* API Key Creation Events

### API Keys

* API Key Generation
* API Key Hashing
* Machine-to-Machine Authentication

---

# Architecture

## Email Authentication Flow

```text
Client
   │
   ▼
POST /auth/login
   │
   ▼
Spring Security
   │
   ▼
Auth Service
   │
   ▼
Validate Credentials
   │
   ▼
Generate JWT
   │
   ▼
Return Access Token
```

---

## Refresh Token Flow

```text
Access Token Expired
          │
          ▼
POST /auth/refresh
          │
          ▼
Validate Refresh Token
          │
          ▼
Generate New Access Token
          │
          ▼
Return New JWT
```

---

## Google OAuth Flow

```text
User
 │
 ▼
Google Login
 │
 ▼
Google Authentication
 │
 ▼
OAuth Success Handler
 │
 ▼
User Provisioning
 │
 ▼
Generate JWT
 │
 ▼
Return Token
```

---

## Session Management Flow

```text
Login
 │
 ▼
Create Refresh Token
 │
 ▼
Create Session
 │
 ▼
Store Session Metadata
```

---

# Tech Stack

## Backend

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Spring OAuth2 Client

## Database

* PostgreSQL
* Flyway

## Authentication

* JWT
* OAuth2
* BCrypt

## Utilities

* Lombok
* Maven

---

# Project Structure

```text
auth/

├── src/
│
├── main/
│
│   ├── java/com/auth/
│   │
│   │   ├── auth/
│   │   ├── user/
│   │   ├── security/
│   │   ├── oauth/
│   │   ├── audit/
│   │   ├── apikey/
│   │   ├── common/
│   │   └── exception/
│   │
│   └── resources/
│       ├── application.yml
│       └── db/migration/
│
├── pom.xml
└── README.md
```

---

# Database Schema

## users

```text
id
email
password_hash
role
created_at
```

---

## refresh_tokens

```text
id
user_id
token
expires_at
created_at
```

---

## sessions

```text
id
user_id
refresh_token_id
device_name
ip_address
created_at
last_active_at
```

---

## oauth_accounts

```text
id
user_id
provider
provider_user_id
created_at
```

---

## audit_logs

```text
id
user_email
action
details
created_at
```

---

## api_keys

```text
id
user_id
key_hash
name
created_at
expires_at
```

---

# API Endpoints

## Authentication

### Register

```http
POST /auth/signup
```

Request

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

---

### Login

```http
POST /auth/login
```

Request

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response

```json
{
  "accessToken": "...",
  "refreshToken": "..."
}
```

---

### Refresh Token

```http
POST /auth/refresh
```

Request

```json
{
  "refreshToken": "..."
}
```

---

# User Endpoints

### Current User

```http
GET /users/me
```

Authorization Required

```http
Authorization: Bearer <token>
```

---

# Session Endpoints

### Get Active Sessions

```http
GET /sessions
```

### Logout Single Session

```http
DELETE /sessions/{sessionId}
```

### Logout All Sessions

```http
DELETE /sessions/all
```

---

# Admin Endpoints

### View Audit Logs

```http
GET /audit
```

ADMIN only.

### Admin Protected Route

```http
GET /admin/users
```

ADMIN only.

---

# API Keys

### Create API Key

```http
POST /apikeys
```

Request

```json
{
  "name": "postman-key"
}
```

Response

```text
sk_live_xxxxxxxxx
```

---

# Security Features

## Password Security

Passwords are never stored in plaintext.

```text
Password
    │
    ▼
BCrypt
    │
    ▼
Database
```

---

## JWT Security

JWT contains:

```text
email
role
issued_at
expiration
```

---

## Refresh Token Security

Refresh tokens are stored in PostgreSQL and can be revoked independently from access tokens.

---

## API Key Security

Only hashed API keys are stored in the database.

```text
API Key
   │
   ▼
BCrypt Hash
   │
   ▼
Database
```

---

# Learning Outcomes

This project demonstrates:

* Authentication vs Authorization
* JWT Lifecycle Management
* Refresh Token Architecture
* OAuth 2.0 Authentication
* Role-Based Access Control
* Session Management
* Audit Logging
* API Key Authentication
* Spring Security Filters
* Database Migrations with Flyway
* Secure Password Storage

---

# Future Improvements

* GitHub OAuth
* Redis Token Blacklisting
* MFA / OTP Authentication
* OpenID Connect
* Rate Limiting
* API Key Rotation
* Docker Deployment
* Kubernetes Deployment

---

# Author

Built as a learning-focused authentication platform to understand how modern production authentication systems are designed and implemented using Spring Boot and Spring Security.
