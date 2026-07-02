INSERT INTO permissions (name, description)
VALUES ('USER_RESET_PASSWORD', 'Reset user passwords')
ON CONFLICT (name) DO UPDATE
SET description = EXCLUDED.description;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'USER_RESET_PASSWORD'
WHERE r.name IN ('ADMIN', 'HEAD_OF_BUREAU')
ON CONFLICT DO NOTHING;
