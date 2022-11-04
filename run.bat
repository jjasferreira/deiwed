@ECHO OFF

:: This script goes to ./src/database and runs the docker-compose command to build the container that holds the database.
IF EXIST src CD src
IF EXIST database CD database
docker-compose up -d --build

:: Then it goes to ./src/backend and runs the maven command to build the backend Spring Boot API that accesses the database.
CD ..
IF EXIST backend CD backend
START mvn clean spring-boot:run

:: Then it goes to ./src/frontend and runs the npm command to build the frontend Vue app that accesses the backend API.
CD ..
IF EXIST frontend CD frontend
START npm install && npm run start

ECHO "Done! Server running at http://localhost:8081"

EXIT