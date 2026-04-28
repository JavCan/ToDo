-- Users
INSERT INTO users (id, full_name, email, active, firebase_uuid, created_at, updated_at, role) 
VALUES ('c4b1236f-b258-410a-8bf8-d6a4a60c07c2', 'Admin User', 'admin@example.com', true, 'abc123firebase', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER');

-- Todos
INSERT INTO todos (id, title, description, completed, created_at, owner_id) 
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Learn Quarkus', 'Study the best Java Framework', false, CURRENT_TIMESTAMP, 'c4b1236f-b258-410a-8bf8-d6a4a60c07c2');

INSERT INTO todos (id, title, description, completed, created_at, owner_id) 
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Implement Fetch Joins', 'Optimize db queries', true, CURRENT_TIMESTAMP, 'c4b1236f-b258-410a-8bf8-d6a4a60c07c2');

-- Categories
INSERT INTO categories (id, name, created_at) 
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'Programming', CURRENT_TIMESTAMP);

INSERT INTO categories (id, name, created_at) 
VALUES ('123e4567-e89b-12d3-a456-426614174001', 'Work', CURRENT_TIMESTAMP);

-- Todos Categories Mapping
INSERT INTO todos_categories (todo_id, category_id) 
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', '123e4567-e89b-12d3-a456-426614174000');

INSERT INTO todos_categories (todo_id, category_id) 
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', '123e4567-e89b-12d3-a456-426614174001');

INSERT INTO todos_categories (todo_id, category_id) 
VALUES ('550e8400-e29b-41d4-a716-446655440000', '123e4567-e89b-12d3-a456-426614174000');

-- Comments
INSERT INTO comments (id, content, author_id, todo_id, created_at) 
VALUES ('11111111-1111-1111-1111-111111111111', 'Great progress!', 'c4b1236f-b258-410a-8bf8-d6a4a60c07c2', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', CURRENT_TIMESTAMP);

INSERT INTO comments (id, content, author_id, todo_id, created_at) 
VALUES ('22222222-2222-2222-2222-222222222222', 'Keep it up!', 'c4b1236f-b258-410a-8bf8-d6a4a60c07c2', '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP);
