# Finshot Bulletin Board - Deployment Guide

This guide explains how to deploy the Finshot Bulletin Board application to a production server. The application is a Spring Boot Java application with a PostgreSQL database, containerized using Docker.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Application Overview](#application-overview)
- [Deployment Steps](#deployment-steps)
  - [1. Setting Up the Server](#1-setting-up-the-server)
  - [2. Preparing the Environment](#2-preparing-the-environment)
  - [3. Configuring the Application](#3-configuring-the-application)
  - [4. Deploying with Docker](#4-deploying-with-docker)
  - [5. Setting Up Nginx as a Reverse Proxy](#5-setting-up-nginx-as-a-reverse-proxy)
  - [6. Securing with SSL/TLS](#6-securing-with-ssltls)
  - [7. Testing the Deployment](#7-testing-the-deployment)
- [Maintenance](#maintenance)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Before you begin, make sure you have:

- A server with:
  - Ubuntu 20.04 or newer (or your preferred Linux distribution)
  - At least 1GB RAM and 10GB storage
  - SSH access with sudo privileges
- Domain name configured to point to your server (bulletin.hapidzfadli.com)
- Basic knowledge of Linux command line, Docker, and networking concepts

## Application Overview

Finshot Bulletin Board is a Java Spring Boot application with the following components:

- **Backend**: Spring Boot application running on port 8081
- **Database**: PostgreSQL database for storing post data
- **Containerization**: Docker and Docker Compose for managing containers
- **Web Server**: Nginx for serving as a reverse proxy (to be installed)

## Deployment Steps

### 1. Setting Up the Server

First, connect to your server via SSH:

```bash
ssh username@your-server-ip
```

Update the system packages:

```bash
sudo apt update && sudo apt upgrade -y
```

Install essential tools:

```bash
sudo apt install -y git curl wget unzip vim
```

### 2. Preparing the Environment

#### Install Docker and Docker Compose

Install Docker:

```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
```

Add your user to the docker group to run Docker without sudo:

```bash
sudo usermod -aG docker $USER
```

Log out and log back in for the group changes to take effect.

Install Docker Compose:

```bash
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.3/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

Verify installations:

```bash
docker --version
docker-compose --version
```

#### Install Nginx

```bash
sudo apt install -y nginx
```

Start and enable Nginx:

```bash
sudo systemctl start nginx
sudo systemctl enable nginx
```

### 3. Configuring the Application

#### Clone the Repository

Create a directory for the application and clone your code:

```bash
mkdir -p /var/www/bulletin
cd /var/www/bulletin
git clone https://your-git-repository-url.git .
```

If you don't have a Git repository, you can simply upload your files using SCP or SFTP:

```bash
# From your local machine
scp -r /path/to/your/project/* username@your-server-ip:/var/www/bulletin/
```

#### Update Docker Compose Configuration

Review and update the `docker-compose.yml` file if needed:

```yaml
version: '3.8'

services:
  app:
    build: .
    container_name: finshot-bulletin-app
    ports:
      - "8081:8081"
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/finshot_db_test
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=Tsubatsa78
      - SERVER_PORT=8081
    restart: always
    networks:
      - finshot-network

  db:
    image: postgres:14
    container_name: finshot-bulletin-db
    environment:
      - POSTGRES_DB=finshot_db_test
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=Tsubatsa78
    ports:
      - "5433:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
    restart: always
    networks:
      - finshot-network

networks:
  finshot-network:
    driver: bridge

volumes:
  postgres-data:
```

> ⚠️ **Important Security Note**: In a production environment, you should:
> 1. Use more secure passwords
> 2. Consider using environment variables or Docker secrets instead of hardcoding credentials
> 3. Limit database port exposure (remove the ports section or only bind to localhost)

### 4. Deploying with Docker

Build and start the Docker containers:

```bash
cd /var/www/bulletin
docker-compose up -d
```

This command:
- Builds the Spring Boot application container
- Creates a PostgreSQL database container
- Sets up the necessary network
- Starts both containers in detached mode

Verify the containers are running:

```bash
docker-compose ps
```

Check the application logs:

```bash
docker-compose logs -f app
```

### 5. Setting Up Nginx as a Reverse Proxy

Create an Nginx configuration file for your domain:

```bash
sudo nano /etc/nginx/sites-available/bulletin.hapidzfadli.com
```

Add the following configuration:

```nginx
server {
    listen 80;
    server_name bulletin.hapidzfadli.com;

    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Enable the site by creating a symbolic link:

```bash
sudo ln -s /etc/nginx/sites-available/bulletin.hapidzfadli.com /etc/nginx/sites-enabled/
```

Test the Nginx configuration:

```bash
sudo nginx -t
```

If the test is successful, reload Nginx:

```bash
sudo systemctl reload nginx
```

### 6. Securing with SSL/TLS

Install Certbot for obtaining a free SSL certificate from Let's Encrypt:

```bash
sudo apt install -y certbot python3-certbot-nginx
```

Obtain a certificate and configure Nginx:

```bash
sudo certbot --nginx -d bulletin.hapidzfadli.com
```

Follow the prompts to complete the certificate setup. Certbot will automatically update your Nginx configuration to use HTTPS.

### 7. Testing the Deployment

Open your browser and navigate to:

```
https://bulletin.hapidzfadli.com
```

You should see your Finshot Bulletin Board application running securely.

## Maintenance

### Updating the Application

To update your application with new code:

1. Pull the latest changes or upload new files to your server
2. Rebuild and restart the containers:

```bash
cd /var/www/bulletin
git pull  # If using Git
docker-compose down
docker-compose up -d --build
```

### Database Backup

To backup the PostgreSQL database:

```bash
docker exec finshot-bulletin-db pg_dump -U postgres finshot_db_test > /path/to/backup.sql
```

Schedule regular backups using cron.

### Monitoring Logs

To view application logs:

```bash
docker-compose logs -f app
```

To view database logs:

```bash
docker-compose logs -f db
```

## Troubleshooting

### Common Issues

1. **Application not starting:**
   - Check the application logs: `docker-compose logs app`
   - Verify database connection settings in `docker-compose.yml`

2. **Database connection errors:**
   - Ensure the database container is running: `docker ps`
   - Check database logs: `docker-compose logs db`

3. **Nginx proxy issues:**
   - Check Nginx error logs: `sudo tail -f /var/log/nginx/error.log`
   - Verify that port 8081 is accessible from Nginx: `curl localhost:8081`

4. **SSL certificate problems:**
   - Run Certbot again: `sudo certbot --nginx -d bulletin.hapidzfadli.com`
   - Check certificate renewal status: `sudo certbot certificates`

### Restarting Services

To restart the entire application:

```bash
cd /var/www/bulletin
docker-compose restart
```

To restart Nginx:

```bash
sudo systemctl restart nginx
```

---

This deployment guide provides a foundation for setting up your Finshot Bulletin Board application. Adjust the configurations as needed for your specific requirements and server environment.
