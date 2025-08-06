# 🧪 Rest Assured API Testing - Platzi Fake Store

A Java project for testing the Platzi Fake Store API using Rest Assured and TestNG.

## ✅ Sample Test Scenarios Covered

- Create, update, delete users  
- Get all categories & create new category  
- Create, update, delete products  
- End-to-End: Create category → Create product → Update product  
- Authentication scenarios  
- Assertion of response body, status codes & performance  

## 🗂 Project Structure

- `POJO` – Data models (User, Product, Category, etc.)  
- `base` – Common setup and configuration  
- `category`, `product`, `user` – Smoke tests for each module  
- `e2e` – End-to-End scenarios  

## ▶️ How to Run Tests

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

### 📊 Generate Allure Report

```bash
allure serve allure-results
```

## ⚙️ Technologies & Tools

| Tool         | Description                           |
|--------------|---------------------------------------|
| Java         | Language used for writing the tests   |
| TestNG       | Test runner and assertions            |
| RestAssured  | HTTP client for REST API testing      |
| Allure       | Advanced test reporting               |
| Maven        | Build and dependency management       |
| Git/GitHub   | Version control                       |
| IntelliJ IDEA| IDE used for development              |

## 📘 API Documentation

[Platzi Fake Store API Docs](https://fakeapi.platzi.com/en/about/introduction/)
