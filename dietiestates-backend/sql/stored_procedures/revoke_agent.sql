CREATE OR REPLACE FUNCTION revoke_agent(user_id_in INTEGER)
RETURNS VOID AS
$$
DECLARE
    roles_to_remove VARCHAR[] := ARRAY['ROLE_AGENT', 'ROLE_ADMIN_ASSISTANT'];
    role_ids_to_remove INTEGER[];
BEGIN
    SELECT ARRAY(
        SELECT id FROM role
        WHERE name = ANY(roles_to_remove)
    ) INTO role_ids_to_remove;

    IF array_length(role_ids_to_remove, 1) IS NOT NULL THEN
        DELETE FROM users_roles
        WHERE
            users_id = user_id_in
            AND roles_id = ANY(role_ids_to_remove);
    END IF;

    UPDATE users
    SET
        agency_id = NULL,
        last_modified_date = NOW()
    WHERE

END;
$$
LANGUAGE plpgsql;