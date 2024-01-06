-- Insert roles if they don't exist
INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN'
);

INSERT INTO roles (name)
SELECT 'ROLE_USER'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_USER'
);

-- Insert admin user if not exists
INSERT INTO users (username, password)
SELECT 'khadir', '$2a$12$7J.epa7n2trMae15XPrEDOKlCwY6X6EvjaoA5BwZ04qjzBvTx88ZO'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'khadir'
);

-- Assign ROLE_ADMIN to 'khadir'
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
CROSS JOIN roles r
WHERE u.username = 'khadir' AND r.name = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM users_roles ur
    JOIN users u2 ON u2.id = ur.user_id
    JOIN roles r2 ON r2.id = ur.role_id
    WHERE u2.username = 'khadir' AND r2.name = 'ROLE_ADMIN'
);
