CREATE DATABASE AmbulanceRequestsDB;

USE AmbulanceRequestsDB;

CREATE TABLE ambulance_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    location VARCHAR(100) NOT NULL,
    specificLocation VARCHAR(100) NOT NULL,
    ambulanceType VARCHAR(50) NOT NULL,
    incidentDescription TEXT NOT NULL,
    patientCondition TEXT NOT NULL,
    requestTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
