# NewsCheck: AI-Based Fake News Detection and Verification Platform

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-20.3.0-red.svg)](https://angular.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue.svg)](https://www.postgresql.org/)

An AI-powered platform that detects fake news and verifies information through reliable sources using Google Custom Search API and the Gemini 2.5 Pro API.

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
- [Alternative Docker Setup](#alternative-docker-setup)
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
git clone https://github.com/neoboi76/ITS120LProj_Grp6.git
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

Create a `application.properties` file in the newscheck (backend) directory

```bash
# For Windows
type nul > src\main\resources\application.properties

# For Mac/Linux
touch src/main/resources/application.properties
```


**Replace all placeholder values with your actual API keys and credentials.**

### Step 3: Build Docker Images

```bash
# Build backend
cd newscheck
docker build -t newscheck .

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

### Alternative Docker Setup

If you prefer not to clone the entire repository, you can run NewsCheck directly using our pre-built Docker images from GitHub Container Registry (GHCR). This method is faster and requires less disk space since you don't need the source code.

#### Prerequisites

- Docker Desktop installed and running
- At least 4GB of RAM allocated to Docker

#### Step 1: Create Project Directory

Create a new directory for your NewsCheck deployment:

```bash
# For Windows
mkdir newscheck-docker
cd newscheck-docker

# For Mac/Linux
mkdir newscheck-docker && cd newscheck-docker
```

#### Step 2: Create Environment File

Create a file named `.env` in your project directory with the following content:

```env
# =========================================================
# = SERVER CONFIGURATION
# =========================================================
server.port=8080

# =========================================================
# = DATABASE CONFIGURATION (PostgreSQL)
# =========================================================
spring.datasource.url=jdbc:postgresql://database:5432/newscheck
spring.datasource.username=newscheck_user
spring.datasource.password=newscheck_password
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
gemini.model.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro-latest:generateContent

# =========================================================
# = GOOGLE SEARCH API CONFIGURATION
# =========================================================
google.search.api.key=YOUR_GOOGLE_SEARCH_API_KEY
google.search.engine.id=YOUR_SEARCH_ENGINE_ID

# =========================================================
# = EMAIL CONFIGURATION
# =========================================================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL_ADDRESS
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

# =========================================================
# = LOGGING
# =========================================================
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

**⚠️ Important:** Replace all placeholder values with your actual API keys and credentials:
- `YOUR_SECRET_KEY_HERE_MINIMUM_256_BITS_LONG_STRING` - A secure random string (at least 32 characters)
- `YOUR_GEMINI_API_KEY` - Your Gemini API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
- `YOUR_GOOGLE_SEARCH_API_KEY` - Your Google Custom Search API key
- `YOUR_SEARCH_ENGINE_ID` - Your Google Search Engine ID
- `YOUR_GMAIL_ADDRESS` - Your Gmail address for sending emails
- `YOUR_GMAIL_APP_PASSWORD` - Your Gmail app-specific password

#### Step 3: Create Docker Compose File

Create a file named `docker-compose.yaml` in the same directory:

```yaml
services:
  database:
    image: postgres:15-alpine
    container_name: newscheck-db
    environment:
      POSTGRES_DB: newscheck
      POSTGRES_USER: newscheck_user
      POSTGRES_PASSWORD: newscheck_password
    ports:
      - "5432:5432"
    volumes:
      - newscheck_postgres_data:/var/lib/postgresql/data
    networks:
      - newscheck-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U newscheck_user -d newscheck"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    image: ghcr.io/neoboi76/its120lproj_grp6/newscheck-backend:latest
    container_name: newscheck-backend
    env_file: .env
    ports:
      - "8080:8080"
    depends_on:
      database:
        condition: service_healthy
    networks:
      - newscheck-network
    restart: unless-stopped

  frontend:
    image: ghcr.io/neoboi76/its120lproj_grp6/newscheck-frontend:latest
    container_name: newscheck-frontend
    ports:
      - "4200:80"
    depends_on:
      - backend
    networks:
      - newscheck-network
    restart: unless-stopped

volumes:
  newscheck_postgres_data:

networks:
  newscheck-network:
    driver: bridge
```

#### Understanding the Docker Compose Configuration

**Services:**

1. **database** (PostgreSQL)
   - Uses the official PostgreSQL 15 Alpine image
   - Port: 5432 (mapped to host)
   - Credentials: `newscheck_user` / `newscheck_password`
   - Database: `newscheck`
   - Includes health checks to ensure it's ready before starting backend

2. **backend** (Spring Boot Application)
   - Uses pre-built image from GitHub Container Registry
   - Port: 8080 (mapped to host)
   - Loads configuration from `.env` file
   - Waits for database to be healthy before starting
   - Auto-restarts unless manually stopped

3. **frontend** (Angular Application)
   - Uses pre-built image from GitHub Container Registry
   - Port: 4200 (mapped from container port 80)
   - Depends on backend service
   - Auto-restarts unless manually stopped

**Volumes:**
- `newscheck_postgres_data`: Persists PostgreSQL database data between container restarts

**Networks:**
- `newscheck-network`: Internal bridge network for inter-container communication

#### Step 4: Start the Application

Run the following command to start all services:

```bash
docker-compose up -d
```

This will:
1. Pull the pre-built images from GitHub Container Registry (first time only)
2. Create and start the database container
3. Wait for the database to be healthy
4. Start the backend container
5. Start the frontend container

#### Step 5: Verify Installation

Check that all containers are running:

```bash
docker-compose ps
```

You should see three containers in "Up" status:
- `newscheck-db`
- `newscheck-backend`
- `newscheck-frontend`

#### Step 6: Access the Application

Once all containers are running:

- **Frontend:** http://localhost:4200
- **Backend API:** http://localhost:8080
- **Database:** localhost:5432

#### Managing the Application

**View logs:**
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f database
```

**Stop the application:**
```bash
docker-compose down
```

**Stop and remove all data (including database):**
```bash
docker-compose down -v
```

**Restart a specific service:**
```bash
docker-compose restart backend
docker-compose restart frontend
```

**Update to latest images:**
```bash
# Pull latest images
docker-compose pull

# Restart with new images
docker-compose up -d
```

#### Troubleshooting

**Images fail to pull:**
- Ensure you have internet connectivity
- Check if GitHub Container Registry is accessible
- Try pulling images manually:
  ```bash
  docker pull ghcr.io/neoboi76/its120lproj_grp6/newscheck-backend:latest
  docker pull ghcr.io/neoboi76/its120lproj_grp6/newscheck-frontend:latest
  ```

**Backend fails to start:**
- Check if `.env` file exists and contains valid API keys
- View backend logs: `docker-compose logs backend`
- Ensure database is healthy: `docker-compose ps database`

**Database connection issues:**
- Verify database health: `docker-compose logs database`
- Ensure the database service is running before backend starts
- Check network connectivity: `docker network inspect newscheck-network`

**Frontend can't connect to backend:**
- Ensure backend is running: `docker-compose ps backend`
- Check backend logs for errors: `docker-compose logs backend`
- Verify port 8080 is not in use by another application

#### Advantages of This Method

✅ **No source code needed** - Just two configuration files  
✅ **Faster setup** - No compilation or building required  
✅ **Smaller disk footprint** - Only stores images and configuration  
✅ **Always up-to-date** - Pull latest images with one command  
✅ **Consistent deployment** - Same images across all environments  

#### When to Use Each Method

**Use Pre-built Images (This Method) when:**
- You want to quickly deploy and test the application
- You don't need to modify the source code
- You want the latest stable version
- You have limited disk space

**Use Full Repository Clone when:**
- You want to contribute to the project
- You need to modify or customize the code
- You want to build images locally
- You're developing or debugging features

---

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
| **[Dino Alfred Timbol](mailto:dattimbol@mymail.mapua.edu.ph)** | Group Leader & Lead Fullstack Developer | Login/Registration/Password functionality, JWT implementation, Email Service implementation, Backend & Frontend architecture |
| **[Ken Aliling](mailto:kbaliling@mymail.mapua.edu.ph)** | Developer | AI-driven News Fact Checker, API integration, Evidence gathering |
| **[Anicia Kaela Bonayao](mailto:aksbonayao@mymail.mapua.edu.ph)** | Developer | Homepage/Verification UI, Settings functionality, Frontend design |
| **[Carl Norbi Felonia](mailto:cncfelonia@mymail.mapua.edu.ph)** | Developer | AI verification pipeline, Reports functionality, Backend services |
| **[Cedrick Miguel Kaneko](mailto:cmpkaneko@mymail.mapua.edu.ph)** | Developer | History page functionality, Database design, Admin features |

**Submitted to:** Prof. Crizepvill Dumalaog  
**Course:** ITS120L - Application Development and Emerging Technologies Laboratory  
**Institution:** *Mapúa University - School of Information Technology*  
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
- **Linkedin:** [Dino Alfred T. Timbol](https://www.linkedin.com/in/dino-alfred-timbol-3b949a248/)
- **GitHub Issues:** [Create an issue](https://github.com/neoboi76/ITS120LProj_Grp6/issues)

---

<p align="center">
  <q>In a world that has really been turned upside down, the true is a moment of the false.</q><br>
  — Guy Debord, <em>The Society of the Spectacle</em><br>
  <em>Fighting fake news, claim at a time.</em><br><br>
</p>

---

## ⭐ Star History

If you find this project useful, please consider giving it a star! ⭐

[![Star History Chart](https://api.star-history.com/svg?repos=neoboi76/ITS120LProj_Grp6&type=Date)](https://star-history.com/#neoboi76/ITS120LProj_Grp6&Date)
