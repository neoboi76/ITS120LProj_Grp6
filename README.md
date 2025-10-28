# NewsCheck: AI-Based Fake News Detection and Verification Platform

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-20.3.0-red.svg)](https://angular.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue.svg)](https://www.postgresql.org/)

An AI-powered platform that detects fake news and verifies information through reliable sources using advanced NLP and the Gemini API.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Local Setup (Manual)](#-local-setup-manual)
  - [Step 1: Clone the Repository](#step-1-clone-the-repository)
  - [Step 2: Database Setup](#step-2-database-setup)
  - [Step 3: Backend Setup](#step-3-backend-setup)
  - [Step 4: Frontend Setup](#step-4-frontend-setup)
- [Docker Setup](#-docker-setup)
- [Project Documentation](#-project-documentation)
- [Team](#-team)
- [Acknowledgements](#-acknowledgements)

---

## 🎯 Overview

NewsCheck is an enterprise-grade fact-checking platform designed to combat misinformation by leveraging AI-powered analysis. The system allows users to submit news content via text, URL, or image upload, and receive instant verification results with detailed reasoning and credible source citations.

**Key Capabilities:**
- Real-time fact-checking using Google's Gemini API
- Multi-format content verification (Text, URL, Image)
- Automated evidence gathering via Google Custom Search
- Comprehensive audit logging system
- Admin dashboard for user and verification management
- JWT-based authentication with role-based access control

---

## ✨ Features

- ✅ **AI-Powered Verification**: Utilizes Google Gemini API for advanced natural language processing
- ✅ **Multi-Source Evidence**: Automatically gathers and analyzes evidence from credible news sources
- ✅ **OCR Capabilities**: Extract and verify text from images using Gemini Vision
- ✅ **Verification History**: Track all past verifications with detailed audit trails
- ✅ **Admin Dashboard**: Comprehensive management tools for users, verifications, and audit logs
- ✅ **Responsive Design**: Modern UI built with Angular and Tailwind CSS
- ✅ **Secure Authentication**: JWT-based auth with token blacklisting on logout

---

## 🛠 Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA** (Hibernate)
- **PostgreSQL** (Database)
- **Jsoup** (Web scraping)
- **Lombok** (Boilerplate reduction)

### Frontend
- **Angular 20.3.0**
- **Tailwind CSS 4.1.14**
- **RxJS** (Reactive programming)
- **Font Awesome** (Icons)

### AI & APIs
- **Google Gemini API** (Content analysis and OCR)
- **Google Custom Search API** (Evidence gathering)

### DevOps
- **Docker & Docker Compose**
- **Maven** (Build tool)
- **npm** (Package manager)

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

### Required Software

| Software | Version | Download Link |
|----------|---------|---------------|
| **Java JDK** | 17+ | [Download](https://www.oracle.com/java/technologies/downloads/#java17) |
| **Node.js** | 18+ | [Download](https://nodejs.org/) |
| **PostgreSQL** | 14+ | [Download](https://www.postgresql.org/download/) |
| **Maven** | 3.8+ | Included in IntelliJ IDEA |
| **Git** | Latest | [Download](https://git-scm.com/) |
| **IntelliJ IDEA** | Community Edition | [Download](https://www.jetbrains.com/idea/download/) |
| **Visual Studio Code** | Latest | [Download](https://code.visualstudio.com/) |
| **Docker Desktop** | Latest (for Docker setup) | [Download](https://www.docker.com/products/docker-desktop) |

### API Keys Required

You will need to obtain the following API keys:

1. **Gemini API Key**
   - Visit: https://makersuite.google.com/app/apikey
   - Create a new API key
   - Enable Gemini API access

2. **Google Custom Search API Key**
   - Visit: https://developers.google.com/custom-search/v1/introduction
   - Create a project in Google Cloud Console
   - Enable Custom Search API
   - Create credentials (API Key)

3. **Google Search Engine ID**
   - Visit: https://programmablesearchengine.google.com/
   - Create a new search engine
   - Configure to search the entire web
   - Copy the Search Engine ID

4. **Gmail App Password** (for email functionality)
   - Visit: https://myaccount.google.com/apppasswords
   - Generate an app-specific password
   - Use this instead of your regular Gmail password

---

## 🚀 Local Setup (Manual)

### Step 1: Clone the Repository

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/newscheck.git

# Navigate to project directory
cd newscheck
```

---

### Step 2: Database Setup

#### Option A: Using pgAdmin 4

1. **Open pgAdmin 4**

2. **Create a new database:**
   - Right-click on "Databases" → Create → Database
   - Database name: `newscheck_db`
   - Owner: `postgres` (or your PostgreSQL username)
   - Click "Save"

3. **Verify connection:**
   - Host: `localhost`
   - Port: `5432`
   - Database: `newscheck_db`
   - Username: `postgres`
   - Password: Your PostgreSQL password

#### Option B: Using SQL Shell (psql)

```sql
-- Open psql and run:
CREATE DATABASE newscheck_db;

-- Verify database creation
\l

-- Connect to the database
\c newscheck_db
```

---

### Step 3: Backend Setup

#### 1. Navigate to Backend Directory

```bash
cd newscheck
```

#### 2. Configure Application Properties

Create a file named `application.properties` in `src/main/resources/`:

```bash
# For Windows
type nul > src/main/resources/application.properties

# For Mac/Linux
touch src/main/resources/application.properties
```

#### 3. Add Configuration

Open `src/main/resources/application.properties` and add:

```properties
# =========================================================
# = SERVER CONFIGURATION
# =========================================================
server.port=8080

# =========================================================
# = DATABASE CONFIGURATION (PostgreSQL)
# =========================================================
spring.datasource.url=jdbc:postgresql://localhost:5432/newscheck_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRESQL_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# =========================================================
# = JPA / HIBERNATE CONFIGURATION
# =========================================================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# =========================================================
# = JWT CONFIGURATION
# =========================================================
jwt.secret=YOUR_SECRET_KEY_HERE_MINIMUM_256_BITS_LONG_STRING
jwt.expiration=86400000

# =========================================================
# = GEMINI API CONFIGURATION
# =========================================================
gemini.api.key=YOUR_GEMINI_API_KEY
gemini.model.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent

# =========================================================
# = GOOGLE SEARCH API CONFIGURATION
# =========================================================
google.search.api.key=YOUR_GOOGLE_SEARCH_API_KEY
google.search.engine.id=YOUR_SEARCH_ENGINE_ID

# =========================================================
# = EMAIL CONFIGURATION
# =========================================================
spring.mail.username=YOUR_GMAIL_ADDRESS
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

**Important Notes:**
- Replace `YOUR_POSTGRESQL_PASSWORD` with your actual PostgreSQL password
- Replace `YOUR_SECRET_KEY_HERE_MINIMUM_256_BITS_LONG_STRING` with a secure random string (at least 32 characters)
- Replace all API keys with your actual keys obtained from the prerequisites section

#### 4. Build and Run Backend (Using IntelliJ IDEA)

**Method 1: Using IntelliJ IDEA**

1. Open IntelliJ IDEA
2. File → Open → Select the `newscheck` folder
3. Wait for Maven to download dependencies (this may take a few minutes)
4. Locate `NewscheckApplication.java` in `src/main/java/com/newscheck/newscheck/`
5. Right-click → Run 'NewscheckApplication'
6. Verify the backend is running at http://localhost:8080

**Method 2: Using Maven Command Line**

```bash
# From the newscheck directory
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

**For Windows:**
```cmd
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

#### 5. Verify Backend is Running

Open your browser and navigate to:
- http://localhost:8080

You should see a 401 Unauthorized or 403 Forbidden response (this is expected - it means the backend is running).

---

### Step 4: Frontend Setup

#### 1. Navigate to Frontend Directory

```bash
# From the project root
cd newscheck-frontend
```

#### 2. Install Dependencies

```bash
npm install
```

This will install all required packages including:
- Angular 20.3.0
- Tailwind CSS 4.1.14
- Font Awesome
- RxJS

**Note:** This may take 5-10 minutes depending on your internet connection.

#### 3. Configure Environment (Optional)

The frontend is pre-configured to connect to `http://localhost:8080`. If you need to change this:

Create `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'
};
```

#### 4. Run the Frontend

```bash
npm start
```

Or explicitly:

```bash
ng serve
```

The Angular development server will start on http://localhost:4200

#### 5. Access the Application

Open your browser and navigate to:
- http://localhost:4200

You should see the NewsCheck login page.

---

### Testing the Application

#### Create an Account

1. Navigate to http://localhost:4200
2. Click "Sign Up"
3. Fill in the registration form:
   - First Name
   - Last Name
   - Email
   - Password (minimum 6 characters)
4. Click "Sign up now"

#### Login

1. Use your registered email and password
2. Click "Login now"
3. You will be redirected to the Home/Dashboard page

#### Admin Access

To access admin features, register with the email: `admin@gmail.com`

This email is hardcoded in the backend to automatically receive admin privileges.

---

## 🐳 Docker Setup

Docker provides a containerized environment that ensures consistency across different machines.

### Prerequisites

- Docker Desktop installed and running
- At least 4GB of RAM allocated to Docker

### Step 1: Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/newscheck.git
cd newscheck
```

### Step 2: Create Environment File

Create a `.env` file in the project root directory:

```bash
# For Windows
type nul > .env

# For Mac/Linux
touch .env
```

Add the following content to `.env`:

```env
# JWT Configuration
JWT_SECRET=YOUR_SECRET_KEY_HERE_MINIMUM_256_BITS_LONG_STRING

# Gemini API Configuration
GEMINI_API_KEY=YOUR_GEMINI_API_KEY
GEMINI_MODEL_URL=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent

# Google Search API Configuration
GOOGLE_SEARCH_API_KEY=YOUR_GOOGLE_SEARCH_API_KEY
GOOGLE_SEARCH_ENGINE_ID=YOUR_SEARCH_ENGINE_ID

# Email Configuration
EMAIL_USERNAME=YOUR_GMAIL_ADDRESS
EMAIL_PASSWORD=YOUR_GMAIL_APP_PASSWORD
```

**Replace all placeholder values with your actual API keys and credentials.**

### Step 3: Build Docker Images

```bash
# Build backend
cd newscheck
docker build -t newscheck-backend .

# Build frontend
cd ../newscheck-frontend
docker build -t newscheck-frontend .

# Return to root
cd ..
```

### Step 4: Run with Docker Compose

```bash
# Start all services (database, backend, frontend)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Step 5: Access the Application

- **Frontend:** http://localhost:4200
- **Backend API:** http://localhost:8080
- **Database:** localhost:5432

### Docker Compose Services

The `docker-compose.yml` file defines three services:

1. **database** (PostgreSQL)
   - Port: 5432
   - Database: `newscheck`
   - Username: `newscheck_user`
   - Password: `newscheck_password`

2. **backend** (Spring Boot)
   - Port: 8080
   - Depends on: database

3. **frontend** (Angular)
   - Port: 4200 (mapped to 80 in container)
   - Depends on: backend

### Troubleshooting Docker

**If containers fail to start:**

```bash
# Check container status
docker-compose ps

# View logs for a specific service
docker-compose logs backend
docker-compose logs frontend
docker-compose logs database

# Restart a specific service
docker-compose restart backend

# Rebuild and restart
docker-compose up -d --build
```

**Database connection issues:**

```bash
# Ensure database is healthy
docker-compose ps

# Check database logs
docker-compose logs database

# Connect to database manually
docker exec -it newscheck-db psql -U newscheck_user -d newscheck
```

**To completely reset:**

```bash
# Stop and remove all containers, networks, and volumes
docker-compose down -v

# Rebuild from scratch
docker-compose up -d --build
```

## 📖 Project Documentation

For detailed project documentation, please refer to:

- **[Project Proposal](./Grp6Timbol_Project%20Proposal.pdf)** - Initial project concept and objectives
- **[Requirements Analysis](./Grp6Timbol_Requirements%20Analysis%20and%20Sprint%20Planning.pdf)** - Functional requirements and sprint planning
- **[System Design](./Grp6Timbol_Prototype,%20ERD,%20and%20Acceptance%20Criteria.pdf)** - ERD, prototypes, and acceptance criteria
- **[Weekly Scrum Reports](./Grp6Timbol_Week8-Scrum.pdf)** - Development progress and sprint retrospectives
- **[Live Demo](#)** - *(Coming Soon)*
- **[Figma Prototype](https://www.figma.com/YOUR_FIGMA_LINK)** - *(Add your Figma link)*

---

## 👥 Team

**Group 6 - ITS120L Project**

| Name | Role | Contributions |
|------|------|---------------|
| **Dino Alfred Timbol** | Group Leader & Lead backend & frontend developer | Login/Registration/Password functionality, JWT implementation, Backend architecture |
| **Ken Aliling** | Developer | AI-driven News Fact Checker, API integration, Evidence gathering |
| **Anicia Kaela Bonayao** | Developer | Homepage/Verification UI, Settings functionality, Frontend design |
| **Carl Norbi Felonia** | Developer | AI verification pipeline, Reports functionality, Backend services |
| **Cedrick Miguel Kaneko** | Developer | History page functionality, Database design, Admin features |

**Submitted to:** Prof. Crizepvill Dumalaog  
**Course:** ITS120L - Applicaiton Development and Emerging Technologies Laboratory
**Institution:** *Mapúa University - School of  Information Technology*  
**Date:** October 2025

---

## 🙏 Acknowledgements

We would like to express our gratitude to:

- **Prof. Crizepvill Dumalaog** - For guidance throughout the project development
- **Google Gemini Team** - For providing the AI API that powers our verification system
- **Spring Boot Community** - For excellent documentation and support
- **Angular Team** - For the robust frontend framework
- **Stack Overflow Community** - For invaluable troubleshooting assistance
- **Micronet Software Manila, Inc.** - For the project concept and business requirements

### Technologies & Libraries

Special thanks to the open-source projects that made this possible:

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Angular](https://angular.io/)
- [Tailwind CSS](https://tailwindcss.com/)
- [PostgreSQL](https://www.postgresql.org/)
- [Jsoup](https://jsoup.org/)
- [JWT](https://jwt.io/)
- [Docker](https://www.docker.com/)
- [Font Awesome](https://fontawesome.com/)

---

## 🔗 Links

- **[GitHub Repository](https://github.com/neoboi76/ITS120LProj_Grp6)**
- **[Live Demo](#)** - *(Coming Soon)*
- **[Project Documentation](#)** - *(Coming Soon)*
- **[Figma Prototype](#)** - *(Coming Soon)*
- **[Report Issues](https://github.com/neoboi76/ITS120LProj_Grp6/issues)**

---

## 📞 Contact

For questions, suggestions, or collaboration opportunities:

- **Email:** *dattimbol@mymail.mapua.edu.ph*
- **GitHub Issues:** [Create an issue](https://github.com/neoboi76/ITS120LProj_Grp6/issues)

---

<p align="center">
  <strong>Made with ❤️ by Group 6</strong><br>
  <em>Fighting misinformation, one verification at a time.</em>
</p>

---

## ⭐ Star History

If you find this project useful, please consider giving it a star! ⭐

[![Star History Chart](https://api.star-history.com/svg?repos=neoboi76/ITS120LProj_Grp6&type=Date)](https://star-history.com/#neoboi76/ITS120LProj_Grp6&Date)
