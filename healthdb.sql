CREATE DATABASE HealthDB;

USE HealthDB;

CREATE TABLE health_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    height INT NOT NULL,
    bloodPressure INT NOT NULL,
    temperature DOUBLE NOT NULL,
    advice TEXT NOT NULL,
    entryTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
