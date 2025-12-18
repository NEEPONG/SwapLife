# SwapLife

SwapLife is a web-based platform designed to facilitate item swapping between users. It allows users to post items they want to swap, browse items from others, and make swap offers.

## Features

- **User Authentication**: Secure registration and login system.
- **Item Management**:
  - Post new items with details (title, description, condition, category, images).
  - Edit and delete your own items.
  - View item details.
  - Search and filter items by keyword, type, category, and condition.
- **Swap System**:
  - Make swap offers on available items.
  - View received and sent offers.
  - (Implied) Accept or reject swap offers.
- **Profile Management**: View your own items (available, swapped, etc.).

## Technology Stack

- **Backend**: Java 22, Spring Boot 3.4.5
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Template Engine**: Thymeleaf
- **Security**: Spring Security
- **Build Tool**: Maven

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 22 or later
- MySQL Server
- Maven

### Installation

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/yourusername/SwapLife.git
    cd SwapLife
    ```

2.  **Database Configuration:**

    - Create a MySQL database named `swaplife`.
    - Update the database credentials in `src/main/resources/application.properties` if they differ from the defaults:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3307/swaplife?characterEncoding=UTF-8&serverTimezone=Asia/Bangkok&useLegacyDatetimeCode=false
      spring.datasource.username=root
      spring.datasource.password=1234
      ```
    - _Note: The default port is configured to 3307. Adjust if your MySQL runs on the standard 3306 port._

3.  **Build the application:**

    ```bash
    mvn clean install
    ```

4.  **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

5.  **Access the application:**
    Open your browser and navigate to `http://localhost:8081`.

## Configuration

- **Server Port**: 8081 (Default)
- **File Uploads**:
  - Images are stored in `./uploads`.
  - Max file size: 10MB.
  - Max request size: 50MB.

## Project Structure

- `src/main/java/com/springboot`: Contains the Java source code.
  - `controller`: Web controllers for handling requests.
  - `model`: Entity classes representing database tables.
  - `repository`: Data access interfaces.
  - `service`: Business logic layer.
  - `config`: Configuration classes.
- `src/main/resources`:
  - `templates`: Thymeleaf HTML templates.
  - `static`: Static assets (CSS, JS, images).
  - `application.properties`: Application configuration.

## License

[MIT License](LICENSE) (Assuming MIT, please verify)
