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

## Schedule an Interview

### Request
`POST /hrims/v1/schedule`

```json
{
  "candidateInfo": {
    "id": 1,
    "name": "Gulam",
    "email": "gulamamu303@gmail.com",
    "position": "Team Lead",
    "resume": "https://example.com/resume.pdf",
    "tech": [
      { "id": 1, "name": "Java" }
    ]
  },
  "interviewersInfo": [
    {
      "id": 1,
      "name": "GULAM MOHEEYUDDIN",
      "emailId": "mahto.mailbox@gmail.com",
      "designation": "Project Manager",
      "department": "Development",
      "tech": [
        { "id": 1, "name": "Java" }
      ]
    }
  ],
  "calendarInfo": {
    "date": "2025-06-24",
    "startTime": "14:00:00",
    "endTime": "15:00:00"
  }
}
```

### Response
```json
{
  "id": 10,
  "candidateInfo": { ... },
  "interviewersInfo": [ ... ],
  "calendarInfo": { ... },
  "calendarLink": "https://calendar.google.com/calendar/event?eid=...",
  "calendarEventId": "abc123def456"
}
```

### Error Responses
- `400 Bad Request`: Invalid input or scheduling conflict
- `500 Internal Server Error`: Unexpected server error or Google Calendar API failure

---

## Update an Interview Schedule

### Request
`PUT /hrims/v1/schedule/{id}`

```json
{
  "candidateInfo": { ... },
  "interviewersInfo": [ ... ],
  "calendarInfo": {
    "date": "2025-06-25",
    "startTime": "15:00:00",
    "endTime": "16:00:00"
  }
}
```

### Response
```json
{
  "id": 10,
  "candidateInfo": { ... },
  "interviewersInfo": [ ... ],
  "calendarInfo": { ... },
  "calendarLink": "https://calendar.google.com/calendar/event?eid=...",
  "calendarEventId": "abc123def456"
}
```

### Error Responses
- `404 Not Found`: Interview not found
- `400 Bad Request`: Invalid update data
- `500 Internal Server Error`: Google Calendar update failed

---

## Delete an Interview Schedule

### Request
`DELETE /hrims/v1/schedule/{id}`

### Response
- `200 OK` if deleted successfully (from both app and Google Calendar)
- `404 Not Found` if the interview does not exist
- `500 Internal Server Error` if Google Calendar deletion fails

---

## Authentication
- The API uses OAuth2 for Google Calendar integration.
- You must authorize the app via `/hrims/v1/oauth2/authorize` before scheduling interviews.
- Only authorized users can create, update, or delete Google Calendar events.

---

## More Sample Data (for H2 database)

### Example `data.sql`
```sql
-- Technologies
INSERT INTO technologies (id, name) VALUES (1, 'Java');
INSERT INTO technologies (id, name) VALUES (2, 'Spring Boot');

-- Candidates
INSERT INTO candidates (id, name, email, position, resume) VALUES (1, 'Gulam', 'gulamamu303@gmail.com', 'Team Lead', 'https://example.com/resume.pdf');
INSERT INTO candidates (id, name, email, position, resume) VALUES (2, 'Test Candidate', 'test2@example.com', 'Developer', 'https://example.com/resume2.pdf');

-- Candidate-Technologies mapping
INSERT INTO candidate_technologies (candidate_id, technology_id) VALUES (1, 1);
INSERT INTO candidate_technologies (candidate_id, technology_id) VALUES (2, 1);
INSERT INTO candidate_technologies (candidate_id, technology_id) VALUES (2, 2);

-- Interviewers
INSERT INTO interviewers (id, name, email_id, designation, department) VALUES (1, 'GULAM MOHEEYUDDIN', 'mahto.mailbox@gmail.com', 'Project Manager', 'Development');
INSERT INTO interviewers (id, name, email_id, designation, department) VALUES (2, 'Test Interviewer', 'interviewer2@example.com', 'Senior Dev', 'Engineering');

-- Interviewer-Technologies mapping
INSERT INTO interviewer_technologies (interviewer_id, technology_id) VALUES (1, 1);
INSERT INTO interviewer_technologies (interviewer_id, technology_id) VALUES (2, 2);
```

---

## Notes
- When you schedule or update an interview, a Google Calendar event is created/updated and invitations are sent to all attendees.
- The `calendarLink` is the direct link to the event in Google Calendar.
- The `calendarEventId` is used internally to update or delete the event from Google Calendar.
- If you use a personal Gmail account, Google Meet links cannot be added programmatically. 