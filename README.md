# Service Booking System (Backend)

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-brightgreen.svg)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-yellow)
![MySQL](https://img.shields.io/badge/MySQL-Database-lightblue)
![Redis](https://img.shields.io/badge/Redis-Caching-red)
![AWS S3](https://img.shields.io/badge/AWS-S3-orange)
![Hibernate](https://img.shields.io/badge/Hibernate-JPA-yellowgreen)
![Swagger](https://img.shields.io/badge/Swagger-API%20Docs-green)

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [How to Run](#how-to-run)
- [License](#license)

## Overview

The **Service Booking System** is a backend application built using **Spring Boot 3** that facilitates service booking interactions between clients and service providers. It allows users to create accounts, list services, book services, and review them. The project implements **JWT-based authentication** for secure access and includes **AWS S3** for image storage and **Redis** for caching to improve performance.

This backend is designed to handle all core business logic, persistence, and communication with front-end clients via RESTful APIs.

## Features

- **User Management**: Registration, login (with JWT), and role-based access control.
- **Service Ads**: Create, update, delete, and manage service advertisements.
- **Service Booking**: Book services, manage booking status, and handle reviews.
- **Review Management**: Clients can review and rate services.
- **Image Upload**: Store and manage service-related images using AWS S3.
- **Data Caching**: Speed up frequently accessed data using Redis.
- **Error Handling**: Unified error handling and validation.

## Technologies Used

- ![Java](https://img.shields.io/badge/Java-17-blue) **Java 17**
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-brightgreen.svg) **Spring Boot 3**
- ![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-yellow) **Spring Security 6** (JWT-based authentication)
- ![Hibernate](https://img.shields.io/badge/Hibernate-JPA-yellowgreen) **Hibernate (JPA)**
- ![MySQL](https://img.shields.io/badge/MySQL-Database-lightblue) **MySQL** (Database)
- ![Redis](https://img.shields.io/badge/Redis-Caching-red) **Redis** (Caching)
- ![AWS S3](https://img.shields.io/badge/AWS-S3-orange) **AWS S3** (Image storage)
- ![ModelMapper](https://img.shields.io/badge/ModelMapper-Object%20Mapping-blue) **ModelMapper** (Object mapping)
- ![Swagger](https://img.shields.io/badge/Swagger-API%20Docs-green) **Swagger** (API documentation)

## How to Run

1. **Clone the repository**: `git clone https://github.com/your-repo/service-booking-system.git`
2. **Database setup**: Set up **MySQL** and **Redis** instances on your local machine or a cloud service.
3. **AWS S3 configuration**: Set up AWS S3 for image storage and configure the credentials in your `application.properties`.
4. **Build and run**: Use Maven to build and run the application.
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
5. **Swagger Documentation**: Navigate to `http://localhost:8080/swagger-ui.html` to access the API documentation.

## License


This project is licensed under the [MIT License](LICENSE).
