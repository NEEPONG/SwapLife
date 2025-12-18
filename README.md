# SwapLife

SwapLife is a **web-based item exchange platform** developed as part of the **Web Programming course**. The project focuses on creating a system where users can exchange items they no longer use instead of buying or throwing them away.

This project is built to practice **Spring Boot**, **MVC architecture**, **database integration**, **security**, and **server-side rendering** using **Thymeleaf**.

## Objectives

* Practice building web applications with Spring Boot
* Apply MVC architecture in a real project
* Use MySQL with Spring Data JPA
* Implement server-side rendering using Thymeleaf
* Experiment with Spring Security and various dependencies
* Apply classroom concepts in a practical project

## Features

* User registration and login
* Post unused items for exchange
* Browse and view available items
* Basic item exchange workflow
* Admin / user role separation (basic)

> Note: This project is developed for educational purposes and focuses on core functionality rather than production-level features.

## Tech Stack

* **Backend:** Spring Boot (Java)
* **Database:** MySQL
* **Template Engine:** Thymeleaf
* **ORM:** Spring Data JPA
* **Security:** Spring Security

## Dependencies Used

This project experiments with multiple Spring Boot dependencies:

* `spring-boot-starter-web` – Web application and MVC support
* `spring-boot-starter-data-jpa` – Database access using JPA
* `mysql-connector-j` – MySQL database driver
* `spring-boot-starter-thymeleaf` – Server-side HTML rendering
* `spring-boot-starter-security` – Authentication and basic security
* `lombok` – Reduce boilerplate code
* `spring-boot-starter-validation` – DTO and form validation
* `spring-boot-devtools` – Development-time tools (hot reload)

## Installation

1. Clone the repository

```bash
git clone https://github.com/your-username/SwapLife.git
cd SwapLife
```

2. Configure MySQL

* Create a MySQL database (e.g. `swaplife_db`)
* Update database credentials in `application.properties` or `application.yml`

3. Run the application

```bash
./mvnw spring-boot:run
```

4. Open your browser

```
http://localhost:8080
```

## Project Scope

* Academic project for Web Programming course
* Focus on backend logic and MVC flow
* Basic UI using Thymeleaf
* Not intended for production use

## Future Improvements

* Item matching and recommendation system
* Image upload for items
* Messaging system between users
* Improved UI/UX
* Notification system

## Author

Developed by SwapLife team (Student Project)

---

This project is created as part of a Web Programming course assignment using Spring Boot.
