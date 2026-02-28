# ProfitFlow - Production Management System

A full-stack web application for production management, built with **Vue 3** (frontend) and **Spring Boot** (backend).

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Features](#features)

## Overview

ProfitFlow is a production management system that allows users to manage products, raw materials, units of measure, and production planning. The application provides a clean, modern interface for inventory management operations.

## Tech Stack

### Backend
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven**

### Frontend
- **Vue 3** (Composition API)
- **Vue Router**
- **Axios**
- **Vite**

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.8+**
- **Node.js 20.19+** or **22.12+**
- **npm 10+**

## Getting Started

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backEnd/api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The backend server will start at `http://localhost:8080`

**Alternative:** You can also run directly from your IDE by executing the `ApiApplication.java` main class.

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontEnd
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The frontend will be available at `http://localhost:5173`

## Project Structure

```
TesteProjedata/
├── backEnd/
│   └── api/
│       ├── src/
│       │   └── main/
│       │       ├── java/com/factory/
│       │       │   ├── config/         # Configuration classes
│       │       │   ├── controller/     # REST controllers
│       │       │   ├── dto/            # Data Transfer Objects
│       │       │   ├── mapper/         # Entity-DTO mappers
│       │       │   ├── model/          # JPA entities
│       │       │   ├── repository/     # Data access layer
│       │       │   └── services/       # Business logic layer
│       │       └── resources/
│       │           └── application.properties
│       └── pom.xml
│
└── frontEnd/
    ├── src/
    │   ├── assets/           # Static assets and icons
    │   ├── components/       # Reusable Vue components
    │   ├── composables/      # Vue composition functions
    │   ├── router/           # Vue Router configuration
    │   ├── services/         # API service layer
    │   ├── views/            # Page components
    │   ├── App.vue           # Root component
    │   └── main.js           # Application entry point
    ├── package.json
    └── vite.config.js
```

## API Endpoints

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create new product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

### Raw Materials
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/raw-materials` | List all materials |
| GET | `/api/raw-materials/{id}` | Get material by ID |
| POST | `/api/raw-materials` | Create new material |
| PUT | `/api/raw-materials/{id}` | Update material |
| DELETE | `/api/raw-materials/{id}` | Delete material |

### Units of Measure
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/units` | List all units |
| GET | `/api/units/{id}` | Get unit by ID |
| POST | `/api/units` | Create new unit |
| PUT | `/api/units/{id}` | Update unit |
| DELETE | `/api/units/{id}` | Delete unit |

### Product Composition
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/product-compositions/{productId}` | Get product compositions |
| POST | `/api/product-compositions` | Add composition to product |
| DELETE | `/api/product-compositions/{productId}/{materialId}` | Remove composition |

## Features

- **Dashboard**: Overview of inventory statistics
- **Products Management**: Create, edit, and delete products with composition
- **Raw Materials**: Manage raw materials inventory with unit associations
- **Units of Measure**: Define measurement units for materials
- **Production Planning**: Optimize production based on available resources
- **Responsive Design**: Works on desktop and mobile devices
- **Auto-generated UI Text**: Dynamic internationalization system

## Running Both Services

For convenience, you can run both services simultaneously:

**Terminal 1 - Backend:**
```bash
cd backEnd/api && mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontEnd && npm run dev
```

Then access the application at `http://localhost:5173`

## Swagger Documentation

API documentation is available at:
```
http://localhost:8080/swagger-ui.html

