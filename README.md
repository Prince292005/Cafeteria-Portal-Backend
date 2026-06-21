Cafeteria-Portal-Backend

The Cafeteria Portal is designed to streamline complaint management and communication between users and administrators. This repository is for the backend, managing data and operations. It aims to provide transparency, efficiency, and automation in handling complaints, feedback, and announcements.

Database Design
<img width="588" height="378" alt="Database Design" src="https://github.com/user-attachments/assets/6bdecbdd-c7d1-46c0-8d30-0ca51d29aa2d" />
Low-Level Design (LLD)
<img width="563" height="726" alt="LLD" src="https://github.com/user-attachments/assets/4c180909-6fef-4d9b-9f98-55d1417cf56d" />


APIs

Currently, the backend has 2 APIs implemented:

1. User Signup

Endpoint: /api/register

Method: POST

Description: Register a new user.

Request Body:

{
"studentId": "string",
"name": "string",
"emailId": "string",
"mobileNumber": 1234567890,
"password": "string"
}


Response:

{
"message": "User registered successfully"
}

2. User Login

Endpoint: /api/login

Method: POST

Description: Authenticate a user and return JWT token.

Request Body:

{
"emailId": "string",
"password": "string"
}


Response:

{
"roles": [
{
"authority": "ROLE_USER"
}
],
"token": "jwt-token",
"studentId": "string"
}

Features

Authentication: JWT-based login for secure access.

User Management: Signup and login functionality.
