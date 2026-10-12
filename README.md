# OpenGround — Large Outdoor Cricket Ground Slot Booking System

**Project Identifier:** 24eg105t41  
**Status:** ✅ Complete & Deployed

---

## 🌐 Live URLs

| Resource | URL |
|---|---|
| **Frontend (Live)** | https://openground-frontend-chi.vercel.app |
| **Backend GitHub** | https://github.com/24eg105t28-rr/openground-backend |
| **Frontend GitHub** | https://github.com/24eg105t28-rr/openground-frontend |

> **Note:** The backend must be deployed separately to a Java-hosting platform (Railway, Render, or a VPS) and the `VITE_API_URL` Vercel environment variable updated to point at it. See deployment section below.

---

## 📋 Project Overview

OpenGround is a full-stack platform exclusively for **large outdoor cricket grounds** (55m+ boundary radius). It supports:

- 🏏 Full-size natural grass and artificial turf ground listings
- 📅 Session-based booking: Morning · Afternoon · Evening Floodlit · Full Day
- 🔐 JWT authentication with Customer, Owner, and Admin roles
- 💳 Pay-at-Ground and Sandbox online payment options
- 🛡️ Concurrency-safe double-booking prevention
- 📊 Owner dashboard with ground management
- 🔧 Admin dashboard with ground approval/suspension

**NOT supported:** Box cricket, indoor cricket, turf cages, batting lanes.

---

## 🛠️ Technology Stack

### Frontend
| Technology | Version |
|---|---|
| React | 19 |
| TypeScript | 5 |
| Vite | 8 |
| Tailwind CSS | 3 |
| Axios | latest |
| React Router DOM | 7 |
| Lucide React | latest |
| React Hot Toast | latest |

### Backend
| Technology | Version |
|---|---|
| Java | 21 LTS |
| Spring Boot | 3.3.4 |
| Spring Web | MVC REST |
| Spring Security | JWT |
| Spring Data JPA | Hibernate |
| PostgreSQL | 15+ |
| Flyway | Migrations |
| JJWT | 0.12.6 |
| SpringDoc OpenAPI | 2.6.0 |

---

## 🚀 Local Development

### Prerequisites
- Java 21 JDK
- Maven 3.9+
- Node.js 18+
- npm 9+
- PostgreSQL 15+

### Backend

```bash
cd openground-backend

# Run with H2 (zero-config, for development)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Run tests
./mvnw test

# The API is available at http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
# H2 Console: http://localhost:8080/h2-console
```

For PostgreSQL production mode, set environment variables:

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:postgresql://localhost:5432/openground
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
export JWT_SECRET=your-256-bit-secret-minimum
./mvnw spring-boot:run
```

### Frontend

```bash
cd openground-frontend
npm install

# Development (with proxy to backend at localhost:8080)
npm run dev

# Production build
npm run build
npm run preview
```

Set your backend URL in `.env.local`:
```env
VITE_API_URL=http://localhost:8080
```

---

## 🗄️ Database

Flyway migrations run automatically on startup:

| Migration | Description |
|---|---|
| `V1__init_schema.sql` | Full schema: users, grounds, slots, bookings, payments, reviews |
| `V2__seed_cricket_grounds.sql` | 5 demo outdoor cricket grounds + demo accounts |

### Demo Accounts (seeded)

| Role | Email | Password |
|---|---|---|
| Customer | customer@openground.in | password123 |
| Ground Owner | owner@centurion.com | password123 |
| Admin | admin@openground.in | admin123 |

---

## 📡 API Reference

All endpoints base: `http://localhost:8080`

### Authentication
```
POST /api/auth/register   Register new account
POST /api/auth/login      Login, returns JWT
GET  /api/auth/me         Get current user (requires JWT)
```

### Grounds
```
GET  /api/grounds                    Search grounds (city, pitchType, floodlights, maxHourlyRate, search)
GET  /api/grounds/{slug}             Ground detail by slug
GET  /api/grounds/{id}/price         Calculate session price (slotId, date params)
```

### Bookings (JWT required)
```
POST /api/bookings                   Create booking
GET  /api/bookings/my                Customer's own bookings
POST /api/bookings/{id}/cancel       Cancel booking
```

### Owner (JWT + OWNER role)
```
GET  /api/owner/grounds              Owner's listed grounds
POST /api/owner/grounds              Create new ground listing
```

### Admin (JWT + ADMIN role)
```
GET  /api/admin/grounds              All grounds
POST /api/admin/grounds/{id}/approve Approve ground
POST /api/admin/grounds/{id}/suspend Suspend ground
GET  /api/admin/users                All users
GET  /api/admin/bookings             All bookings
```

### Health
```
GET  /api/health                     System health check
```

Full interactive docs at: `http://localhost:8080/swagger-ui.html`

---

## 🌐 Deployment

### Frontend (Vercel) — Already Deployed

The frontend is live at **https://openground-frontend-chi.vercel.app**

To update the backend API URL after deploying the backend:
1. Go to [Vercel Dashboard](https://vercel.com)
2. Select `openground-frontend`
3. Settings → Environment Variables
4. Add `VITE_API_URL` = `https://your-backend-domain.com`
5. Redeploy

### Backend — Railway (Recommended)

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login and deploy
railway login
cd openground-backend
railway init
railway add postgresql
railway up

# Set environment variables in Railway dashboard:
# SPRING_PROFILES_ACTIVE=prod
# JWT_SECRET=<generate 256-bit random string>
```

### Backend — Render

1. New Web Service → Connect GitHub repo `openground-backend`
2. Runtime: Docker (use the Dockerfile below) or Java
3. Build Command: `./mvnw clean package -DskipTests`
4. Start Command: `java -jar target/openground-backend-1.0.0.jar`
5. Add PostgreSQL database in Render dashboard
6. Set env vars: `SPRING_PROFILES_ACTIVE=prod`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`

### Dockerfile (for containerized deployment)

```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/openground-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🧪 Tests

```bash
# Backend: 3 tests covering auth, context load, double-booking prevention
./mvnw test

# Results: Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

Test classes:
- `OpenGroundApplicationTests` — Spring Boot context loads
- `AuthIntegrationTest` — Register + Login flow
- `DoubleBookingPreventionTest` — Concurrent slot conflict detection

---

## 📁 Project Structure

```
openground-backend/
├── src/main/java/com/openground/
│   ├── controller/     # REST endpoints (Auth, Grounds, Bookings, Owner, Admin)
│   ├── service/        # Business logic
│   ├── repository/     # JPA repositories
│   ├── entity/         # JPA entities & enums
│   ├── dto/            # Request/Response DTOs
│   ├── exception/      # Custom exceptions + GlobalExceptionHandler
│   ├── security/       # JWT, UserDetails, SecurityConfig
│   └── config/         # OpenAPI config
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/   # Flyway SQL migrations

openground-frontend/
├── src/
│   ├── api/            # Axios client with JWT interceptor
│   ├── components/     # Navbar, Footer, ProtectedRoute
│   ├── context/        # AuthContext
│   ├── pages/          # All page components
│   ├── types/          # TypeScript types
│   └── utils/          # Formatting helpers
├── tailwind.config.js
├── vite.config.ts
└── vercel.json
```

---

## 📝 Notes

- **Large outdoor grounds only:** The backend enforces a minimum 55m boundary radius for all ground listings.
- **Anti-double-booking:** Uses synchronized Java blocks + PostgreSQL overlap query to prevent concurrent overlapping bookings.
- **Price recalculation:** All prices are calculated server-side; client-supplied prices are never trusted.
- **Seeded data** in `V2__seed_cricket_grounds.sql` is clearly marked as demonstration data.
