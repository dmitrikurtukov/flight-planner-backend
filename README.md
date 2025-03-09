# Flight Planner Backend

This is the backend service for the **Flight Planner** application. It provides REST APIs for retrieving all or filtered flights and seats.
The application allows users to choose a flight and receive seat recommendations based on available seats. 
Flights can be filtered by destination, departure date, duration, and price, and sorted by price or departure date in both ascending and descending order. 
Seat recommendations consider user preferences such as window seats, extra legroom, proximity to exits, and keeping seats together if traveling in a group. 
Flights and occupied seats are generated randomly.

## Tech Stack
- **Java 23**
- **Spring Boot 3.4.3**
- **Spring Data JPA** (PostgreSQL)
- **Lombok**
- **Liquibase** (for database migrations)
- **MapStruct** (for DTO mapping)
- **JUnit 5 + Mockito** (for testing)
- **Springdoc OpenAPI** (for API documentation)
- **Docker** (for running the database and application in containers)

---

## How to Run the Application

Make sure you have **Java 23** and **Docker Desktop** installed.

1. **Clone the repository** into IntelliJ or any other IDE.
2. **Build the application** by running the following command in the terminal:
    ```sh
   ./gradlew clean build -x test
3. **Create and run backend and database Docker containers** by executing:
    ```sh
   docker-compose up -d --build

If everything is set up correctly, you should be able to see running backend and database containers in Docker Desktop and also be able to send HTTP requests to the backend using **Swagger UI** at:

http://localhost:8080/swagger-ui.html

**P.S.** You can also run the backend application directly in IntelliJ instead of using a Docker container. Simply run the `FlightPlannerBackendApplication` class.  
I highly recommend running the backend this way (while keeping the database container running in Docker) because, in some cases, unexpected errors occur when running it as a container, and I haven't been able to determine the exact cause.


---

## Development Process

### Time Spent on the Project (Backend Part): 30+ hours

### Challenges and How They Were Solved
- **CORS Issues:** Initially, frontend requests were being blocked due to cross-origin restrictions. To resolve this, I implemented a CORS configuration to allow requests from different origins.
- **Complex Seat Filtering Logic:** The `filterSeatsTogether()` method did not behave as expected, requiring multiple adjustments. Debugging took considerable time, I used ChatGPT for assistance. While the method still has some issues when combined with other filters, it works well enough on its own.
- **Dependency Compatibility Issues:** The application used to fail to start a lot of times due to incompatible dependency versions. I had to research and update to the latest stable versions that worked well together.
