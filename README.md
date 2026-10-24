# GoShortly — URL Shortener Backend

A scalable REST API for shortening long URLs, managing user authentication, and tracking URL analytics. Built with **Java and Spring Boot**, GoShortly provides secure URL management, unique short links, and click-tracking capabilities.

## 🚀 Features

- **URL Shortening** — Generate unique short codes for long URLs.
- **User Authentication** — Secure authentication using JWT and Spring Security.
- **Role-Based Access Control** — Protect endpoints using user roles and authorization rules.
- **URL Redirection** — Redirect short links to their original destinations.
- **Click Analytics** — Track URL performance and access statistics.
- **RESTful APIs** — Clean API architecture for frontend integration.
- **Database Persistence** — Store users, URLs, and click events using relational databases.
- **Redis Caching** — Support low-latency retrieval of frequently accessed URLs.
- **Environment-Based Configuration** — Separate development and production settings.
- **Production Deployment** — Deployable using Docker and cloud hosting platforms.

## 🛠️ Tech Stack

| Category | Technologies |
|---|---|
| Language | Java |
| Backend Framework | Spring Boot |
| Security | Spring Security, JWT |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL, PostgreSQL |
| Caching | Redis |
| API Development | REST APIs |
| Build Tool | Maven |
| Testing & API Debugging | Postman |
| Containerization | Docker |
| Deployment | Render |

## 🏗️ Architecture

```text
                 Client / Frontend
                         |
                         v
                  Spring Boot API
                         |
             +-----------+-----------+
             |           |           |
             v           v           v
        Spring       URL Service   Analytics
        Security         |           |
             |           v           v
             |        Database   Click Events
             |           |
             +-----------+
                         |
                         v
                    Redis Cache
```

*Note: This is a conceptual overview; the exact request flow depends on the implemented services and cache integration.*

## 📁 Project Structure

```text
url-shortener-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── .../
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       ├── service/
│   │   │       └── exception/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── Dockerfile
├── pom.xml
└── README.md
```

*Adjust the package and directory names to match your actual project structure.*

## ⚙️ Getting Started

### Prerequisites

- Java 17 or the Java version configured by the project
- Maven
- MySQL or PostgreSQL
- Redis (if caching is enabled)

### 1. Clone the Repository

```bash
git clone <YOUR_BACKEND_REPOSITORY_URL>
cd <YOUR_BACKEND_DIRECTORY>
```

### 2. Configure Environment Variables

Configure your database, JWT secret, and Redis connection through environment variables or your local configuration file.

Example:

```env
SERVER_PORT=8080
DB_URL=jdbc:mysql://localhost:3306/shortly
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_secure_secret
REDIS_HOST=localhost
REDIS_PORT=6379
```

Update the values to match the property names used in your Spring Boot configuration. For PostgreSQL, use the appropriate JDBC URL and database driver.

**Security:** Never commit database credentials, JWT secrets, or other sensitive configuration to GitHub.

### 3. Build the Application

```bash
./mvnw clean package
```

On Windows:

```powershell
.\mvnw.cmd clean package
```

If the Maven Wrapper is not included, use `mvn clean package`.

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The backend will start on the configured port, typically `8080`.

## 🔗 API Endpoints

The following table describes the core API operations. Verify the paths against your actual controller mappings.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1.0/auth/register` | Register a user |
| POST | `/api/v1.0/auth/login` | Authenticate a user |
| POST | `/api/v1.0/shorten` | Generate a short URL |
| GET | `/{shortCode}` | Redirect to the original URL |
| GET | `/api/v1.0/analytics/{shortCode}` | Retrieve URL analytics |
| GET | `/api/v1.0/urls` | Retrieve a user's URLs |

### Example: Shorten a URL

**Request**

```http
POST /api/v1.0/shorten
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

```json
{
  "originalUrl": "https://example.com/articles/backend-development"
}
```

**Example response**

```json
{
  "shortCode": "aB12xY",
  "shortUrl": "https://your-domain.com/aB12xY",
  "originalUrl": "https://example.com/articles/backend-development"
}
```

*The request fields, response structure, and endpoint paths are illustrative; align them with your actual DTOs and controllers.*

## 📊 URL Analytics

The analytics functionality can provide insights into shortened URL usage, including:

- Total clicks per short URL
- Click event history
- URL performance over time
- Data for analytics dashboards

Actual metrics depend on the fields recorded by the application.

## 🔐 Security

- JWT-based authentication for protected operations.
- Spring Security authorization rules for API access.
- Role-based access control where configured.
- Secure password storage using BCrypt.
- Externalized secrets and environment-specific configuration.

## 🌍 Deployment

The backend can be deployed to a cloud platform such as Render.

Before deploying:

1. Configure production database credentials.
2. Set the required environment variables.
3. Configure the production database JDBC URL.
4. Set the frontend's permitted CORS origin.
5. Configure Redis if used in production.
6. Verify health checks, authentication, redirects, and analytics endpoints.

## 🧪 Testing

Use Postman or another HTTP client to test:

- User registration and login
- JWT-protected endpoints
- URL creation and redirection
- Unauthorized and forbidden requests
- Analytics retrieval
- Database persistence and cache behavior

## 🔮 Future Improvements

- Rate limiting and abuse prevention
- Custom aliases and configurable link expiration
- QR code generation
- Asynchronous click-event processing with Apache Kafka
- Advanced analytics dashboards
- Automated integration testing
- API documentation using OpenAPI/Swagger

## 👨‍💻 Author

**Developer:** [Your Name](https://github.com/devch105)

**GitHub:** [github.com/devch105](https://github.com/devch105)

---

⭐ If you find this project useful, consider giving the repository a star.
