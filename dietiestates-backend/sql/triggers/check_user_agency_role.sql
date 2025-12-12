CREATE OR REPLACE FUNCTION check_user_agency_role()
RETURNS trigger AS $$
DECLARE
    allowed_role_count INTEGER;
BEGIN
    -- Se l'utente NON sta inserendo una agency_id, allora non serve nessun controllo
    IF NEW.agency_id IS NULL THEN
        RETURN NEW;
    END IF;

    -- Controlliamo se l’utente ha almeno uno dei due ruoli ammessi
    SELECT COUNT(*) INTO allowed_role_count
    FROM users_roles ur
    JOIN role r ON ur.roles_id = r.id
    WHERE ur.users_id = NEW.id
      AND (r.name = 'ROLE_AGENT' OR r.name = 'ROLE_ADMIN');

    -- Se l'utente non ha i ruoli richiesti -> blocchiamo l'inserimento
    IF allowed_role_count = 0 THEN
        RAISE EXCEPTION 'L’utente deve avere ROLE_AGENT o ROLE_ADMIN per essere assegnato ad una agenzia';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_user_agency_role
BEFORE INSERT OR UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION check_user_agency_role();

