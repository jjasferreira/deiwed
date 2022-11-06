# DEIWed

**DEIWed: Wednesdays @ DEI Management System** is a simple web application project that aims to facilitate the management of DEI sessions on Wednesdays.

## A look at what's going on

The frontend is a Vue application that uses the backend, a Spring Boot application. The user interacts with the frontend, which communicates with the backend, to manage data through a REST API. This backend application uses JPA to connect to a database and manage entities. DEIWed uses a Docker containerized MariaDB database to persist data.

[Here](NOTES.md), you can find some notes about the application's development process and features.

## Configuring

Before being able to run the application, you must open Docker Desktop.

Please make sure you have `docker`, `maven` and `npm` installed and added to the PATH system variable.

## Running

To run the application on Windows, simply run:

```powershell
.\run.bat
```

in this folder, after opening Docker Desktop on the background.
