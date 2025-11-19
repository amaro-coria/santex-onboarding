-- Seed data for development and testing
-- Note: In production, this should be run separately or conditionally

-- Insert sample users (password is 'password123' - should be hashed in real application)
INSERT INTO users (email, password, first_name, last_name, created_at, updated_at)
VALUES
    ('john.doe@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('jane.smith@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane', 'Smith', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('bob.wilson@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bob', 'Wilson', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample tasks
INSERT INTO tasks (title, description, status, user_id, due_date, created_at, updated_at)
VALUES
    ('Setup Development Environment', 'Install and configure all required tools and dependencies', 'DONE', 1, '2025-11-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Implement User Authentication', 'Add JWT-based authentication system', 'IN_PROGRESS', 1, '2025-11-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Create REST API Endpoints', 'Develop CRUD endpoints for all entities', 'TODO', 2, '2025-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Write Unit Tests', 'Add comprehensive unit tests for services and controllers', 'TODO', 2, '2025-12-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Design Frontend UI', 'Create wireframes and mockups for the application', 'IN_PROGRESS', 3, '2025-11-28', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Setup CI/CD Pipeline', 'Configure GitHub Actions for automated testing and deployment', 'TODO', NULL, '2025-12-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
