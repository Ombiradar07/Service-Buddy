# Service Booking System (Backend)

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-brightgreen.svg)
![Redis](https://img.shields.io/badge/Redis-Caching-red)
![AWS S3](https://img.shields.io/badge/AWS-S3-orange)

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Caching and Image Storage](#caching-and-image-storage)
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

- **Java 17**
- **Spring Boot 3**
- **Spring Security 6** (JWT-based authentication)
- **Hibernate (JPA)**
- **MySQL** (Database)
- **Redis** (Caching)
- **AWS S3** (Image storage)
- **ModelMapper** (Object mapping)
- **Swagger** (API documentation)

## Installation

### Prerequisites

- Java 17+
- Maven
- MySQL
- Redis
- AWS S3 account for image storage
