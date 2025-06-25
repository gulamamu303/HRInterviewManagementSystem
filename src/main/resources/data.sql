-- Sample Technologies
INSERT INTO technologies (id, name) VALUES (1, 'Java');
INSERT INTO technologies (id, name) VALUES (2, 'Spring Boot');

-- Sample Candidates
INSERT INTO candidates (id, name, email, position, resume) VALUES (1, 'Gulam', 'gulamamu303@gmail.com', 'Developer', 'https://example.com/resume.pdf');
INSERT INTO candidates (id, name, email, position, resume) VALUES (2, 'Santosh', 'mahto.mailbox@gmail.com', 'Team Lead', 'https://example.com/resume2.pdf');

-- Candidate-Technologies mapping
INSERT INTO candidate_technologies (candidate_id, technology_id) VALUES (1, 1);
INSERT INTO candidate_technologies (candidate_id, technology_id) VALUES (2, 1);
INSERT INTO candidate_technologies (candidate_id, technology_id) VALUES (2, 2);

-- Sample Interviewers
INSERT INTO interviewers (id, name, email_id, designation, department) VALUES (1, 'Rituraj', '12pulkit12@gmail.com', 'Project Manager', 'Development');
INSERT INTO interviewers (id, name, email_id, designation, department) VALUES (2, 'Test Interviewer', 'interviewer2@example.com', 'Senior Dev', 'Engineering');

-- Interviewer-Technologies mapping
INSERT INTO interviewer_technologies (interviewer_id, technology_id) VALUES (1, 1);
INSERT INTO interviewer_technologies (interviewer_id, technology_id) VALUES (2, 2); 