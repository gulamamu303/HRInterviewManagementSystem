# HR Interview Management System

A Spring Boot application for managing HR interviews, including candidate information, interviewer assignments, and interview scheduling with Google Meet integration.

## Features

- Technology management
- Candidate management
- Interviewer management
- Interview scheduling with Google Calendar integration
- Email notifications and calendar invites

## Prerequisites

- Java 17 or higher
- Maven
- Google Calendar API credentials

## Setup

1. Clone the repository
2. Configure Google Calendar API:
   - Create a project in Google Cloud Console
   - Enable Google Calendar API
   - Create OAuth 2.0 credentials
   - Download the credentials file and save it as `credentials.json` in the project root

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will be available at `https://localhost:8080/hrims/v1`

## API Endpoints

### Technology Management
- `POST /tech` - Add a new technology
- `GET /tech` - Get all technologies
- `PUT /tech/{id}` - Update a technology
- `DELETE /tech/{id}` - Delete a technology

### Candidate Management
- `POST /candidate` - Add a new candidate
- `GET /candidate` - Get all candidates
- `PUT /candidate/{id}` - Update a candidate
- `DELETE /candidate/{id}` - Delete a candidate

### Interviewer Management
- `POST /interviewer` - Add a new interviewer
- `GET /interviewer` - Get all interviewers
- `PUT /interviewer/{id}` - Update an interviewer
- `DELETE /interviewer/{id}` - Delete an interviewer

### Interview Scheduling
- `POST /schedule` - Schedule a new interview
- `GET /schedule` - Get all scheduled interviews
- `PUT /schedule/{id}` - Update an interview schedule
- `DELETE /schedule/{id}` - Delete an interview schedule

## Database

The application uses H2 in-memory database for development. The H2 console is available at `https://localhost:8080/hrims/v1/h2-console`

## Security

The application uses OAuth 2.0 for Google Calendar API integration. Make sure to:
1. Keep your credentials file secure
2. Configure proper CORS settings for production
3. Implement proper authentication and authorization

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request 