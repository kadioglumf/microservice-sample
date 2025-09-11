# Project Backend Setup with Docker

Follow the steps below to run the project using Docker.

## Prerequisites

- Ensure Docker is installed on your system.
- Verify Docker Compose is available (comes with Docker Desktop by default).

## Steps to Run the Project

1. Navigate to the `.dev` directory:
   ```bash
   cd .dev
   ```

2. Build the Docker images:
   ```bash
   docker compose build
   ```

3. Start the containers in detached mode:
   ```bash
   docker compose up -d
   ```

## Additional Information

- Check running containers:
  ```bash
  docker ps
  ```

- View logs for a specific container:
  ```bash
  docker logs <container_name>
  ```

- To stop the containers, run:
  ```bash
  docker compose down
  ```

