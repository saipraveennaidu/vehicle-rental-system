# Vehicle Rental System

A **Spring Boot-based backend application** for managing vehicle rentals. This system provides functionalities for user authentication, vehicle management, bookings, reviews, and role-based access control (RBAC).

---

## Features

- **User Management**: Register, login, and manage user profiles.
- **Vehicle Management**: Add, update, delete, and view vehicles.
- **Booking Management**: Create, view, and cancel bookings.
- **Review System**: Users can leave reviews for vehicles.
- **Authentication & Authorization**:
  - JWT-based authentication
  - Role-based access control (Admin, User)
- **REST API Endpoints**: Fully implemented for all operations.
- **Database**: MySQL with JPA/Hibernate for ORM.
- **Exception Handling**: Global exception handling for cleaner error responses.

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Security (JWT)
- JPA / Hibernate
- MySQL
- Maven
- Lombok

---

## Project Structure

src/
├─ main/
│ ├─ java/com/rental/vehicle_rental_system/
│ │ ├─ Controller/
│ │ ├─ Dto/
│ │ ├─ Entity/
│ │ ├─ Exception/
│ │ ├─ Repository/
│ │ ├─ Security/
│ │ └─ Service/
│ └─ resources/
│ └─ application.properties
└─ test/
└─ java/com/rental/vehicle_rental_system/

---

## Setup Instructions

1. **Clone the repository**
```bash
  git clone https://github.com/saipraveennaidu/vehicle-rental-system.git
  cd vehicle-rental-system

Configure MySQL
  Create a database named vehicle_rental_db.
  Update src/main/resources/application.properties with your MySQL username and password.

Build & Run
  mvn clean install
  mvn spring-boot:run

Access API
  Base URL: http://localhost:8080/api/
Use tools like Postman to test endpoints.

API Endpoints
Auth
  POST /api/auth/register – Register a new user
  POST /api/auth/login – Authenticate user
Vehicle
  GET /api/vehicles – List all vehicles
  POST /api/vehicles – Add a vehicle (Admin)
  GET /api/vehicles/{id} – Get vehicle details
  PUT /api/vehicles/{id} – Update vehicle (Admin)
  DELETE /api/vehicles/{id} – Delete vehicle (Admin)
Booking
  POST /api/bookings – Create booking
  GET /api/bookings/user/{userId} – List bookings by user
  DELETE /api/bookings/{id} – Cancel booking
Review
  POST /api/reviews – Add review
  GET /api/reviews/vehicle/{vehicleId} – Get reviews by vehicle

License
This project is open-source and free to use.

Author
  Pudi Sai Praveen Naidu
  Email: saipraveennaidupudi@gmail.com
  GitHub: https://github.com/saipraveennaidu
