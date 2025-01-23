CREATE TABLE notes (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    content VARCHAR(500),
    date TIMESTAMP NOT NULL,
    user_id UUID,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);
