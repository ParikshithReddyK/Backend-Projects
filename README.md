# Campus Workforce Management Platform

A microservices-based backend system for managing the student employment lifecycle across a university campus — from job posting through to payroll — built with Java 21, Spring Boot, and Spring Cloud Gateway.

## Overview

This platform models a real campus workforce pipeline: HR and Supervisors post jobs, students browse and apply, approved applicants get scheduled shifts, students clock in/out, and HR generates payroll computed directly from actual worked hours. Each stage is owned by an independently deployable service with its own database, communicating over authenticated HTTP calls — not a shared database or a monolith split into packages.

## Architecture

Seven Spring Boot services sit behind a single API Gateway, each with its own MySQL database:

| Service | Port | Responsibility |
|---|---|---|
| **API Gateway** | 8080 | Single entry point; routes requests to the correct service by path |
| **User Service** | 8081 | Registration, login, JWT issuance, role-based access control |
| **Job Service** | 8082 | Job postings — create, browse, update, close |
| **Application Service** | 8083 | Students apply to jobs; HR approves/rejects |
| **Shift Service** | 8084 | Scheduling shifts for accepted applicants |
| **Attendance Service** | 8085 | Clock-in / clock-out against a scheduled shift |
| **Payroll Service** | 8086 | Computes pay from real attendance records and job rates |

```
Client
  │
  ▼
API Gateway (8080)
  │
  ├──▶ User Service (8081) ──────────────┐
  ├──▶ Job Service (8082)                │
  ├──▶ Application Service (8083) ───▶ Job Service (validates job)
  ├──▶ Shift Service (8084) ─────────▶ Application Service (validates ACCEPTED status)
  ├──▶ Attendance Service (8085) ────▶ Shift Service (validates assignment)
  └──▶ Payroll Service (8086) ───────▶ Job Service + Attendance Service (rate + hours)
                                          │
                                    (JWT issued by User Service, propagated on every call)
```

### Key design decisions

- **True microservices, not a modular monolith.** Each service has its own MySQL database. No service reads another's tables directly or shares a schema.
- **No foreign keys across service boundaries.** A `Job` doesn't hold a `User` entity — it holds a plain `postedBy` ID. Services that need cross-service data call the owning service's REST API.
- **Stateless JWT authentication with a shared secret (HS256).** User Service issues tokens embedding the user's ID and role. Every other service independently verifies the same token — no session state, no per-service login.
- **Token propagation for service-to-service calls.** When Shift Service needs to check an application's status, it forwards the *caller's own token* to Application Service rather than maintaining separate service credentials.
- **Ownership-based authorization**, not just role checks. A Supervisor can only edit jobs *they* posted; an admin can act on anything. This is enforced by fetching the resource's owner from the source-of-truth service and comparing against the caller's ID from their JWT.
- **Payroll snapshots the rate and hours used**, rather than storing only the final pay figure — so a later rate change never silently rewrites historical payroll.

## Tech Stack

- **Language / Framework:** Java 21, Spring Boot 4.1
- **Data:** Spring Data JPA, Hibernate, MySQL 8
- **Security:** Spring Security, JWT (jjwt)
- **Gateway:** Spring Cloud Gateway (WebFlux)
- **Inter-service HTTP:** Spring's `RestClient`
- **Containerization:** Docker, Docker Compose
- **Build:** Maven (multi-module monorepo)

## Running the project

### Prerequisites
- Docker Desktop installed and running

### Start everything
```bash
git clone https://github.com/ParikshithReddyK/Backend-Projects.git
cd Backend-Projects/campus-workforce-management
cp .env.example .env   # fill in DB_PASSWORD and JWT_SECRET
docker compose up --build
```

This builds all seven services and starts them alongside a MySQL instance, fully networked. All requests should go through the gateway on `http://localhost:8080`.

### Example: full lifecycle via the gateway

```bash
# Register and log in
curl -X POST http://localhost:8080/api/users/register -H "Content-Type: application/json" \
  -d '{"fullName":"Admin User","email":"admin@campus.edu","password":"adminpass123","role":"ADMIN"}'

curl -X POST http://localhost:8080/api/users/login -H "Content-Type: application/json" \
  -d '{"email":"admin@campus.edu","password":"adminpass123"}'
# → returns a JWT; use it as a Bearer token on subsequent requests
```

From there: post a job (`POST /api/jobs`), have a student apply (`POST /api/applications`), approve it, schedule a shift (`POST /api/shifts`), clock in/out (`POST /api/attendance/clock-in`, `PATCH /api/attendance/clock-out`), and generate payroll (`POST /api/payroll/generate`).

## Project structure

```
campus-workforce-management/
├── api-gateway/
├── user-service/
├── job-service/
├── application-service/
├── shift-service/
├── attendance-service/
├── payroll-service/
├── docker-compose.yml
├── mysql-init/          # creates per-service databases on first boot
└── pom.xml               # parent POM (packaging=pom, shared dependency versions)
```

Each service module follows the same internal layering: `controller → service → repository`, with `dto`, `mapper`, `model`, `security`, `config`, and `exception` packages alongside.

## Status

All seven services are complete, tested end-to-end (including duplicate-prevention, ownership checks, and cross-service validation), and fully containerized.
