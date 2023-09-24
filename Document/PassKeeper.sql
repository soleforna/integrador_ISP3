-- Setear la base de datos por defecto
SET search_path TO passkeeper;

------------- TABLAS -------------

-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS public."users" (
        id uuid primary KEY,
        email varchar UNIQUE NOT NULL,
        pwd varchar NOT null,
        created_at Timestamp null,
		updated_at Timestamp null
       );

-- Tabla Passwords
CREATE TABLE public."password" (
	id uuid primary KEY,
	url varchar NULL,
	username varchar NULL,
	keyword varchar not NULL,
	description varchar null,
	category varchar null,
	user_id uuid,
    FOREIGN KEY (user_id) REFERENCES users (id),
	created_at Timestamp null,
	updated_at Timestamp null
);

------------- FUNCIONES -------------

-- Funcion para insertar un Usuario
CREATE OR REPLACE FUNCTION insert_user(
    id uuid, email varchar, pwd varchar,
    created_at timestamp, updated_at timestamp)
RETURNS void AS $$
BEGIN
    INSERT INTO users (id, email, pwd, created_at, updated_at)
    VALUES (id, email, pwd, created_at, updated_at);
END;
$$ LANGUAGE plpgsql;

-- Funcion para insertar un Password
CREATE OR REPLACE FUNCTION insert_password(
    id uuid, url varchar, username varchar, keyword varchar,
    description varchar, category varchar, user_id uuid,
    created_at timestamp, updated_at timestamp)
RETURNS void AS $$
BEGIN
    INSERT INTO password (id, url, username, keyword, description, category, user_id, created_at, updated_at)
    VALUES (id, url, username, keyword, description, category, user_id, created_at, updated_at);
END;
$$ LANGUAGE plpgsql;

-- Funcion para obhtener todos los Password de un usuario
CREATE OR REPLACE FUNCTION get_passwords_by_user_id(in_user_id UUID)
RETURNS TABLE (
    id UUID,
    url VARCHAR,
    username VARCHAR,
    keyword VARCHAR,
    description VARCHAR,
    category VARCHAR,
    user_id UUID,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY SELECT * FROM "password" WHERE "password".user_id = in_user_id;
END;
$$ LANGUAGE plpgsql;

-- Funcion para actualizar un Password
CREATE OR REPLACE FUNCTION update_password(
    p_id uuid, p_url varchar, p_username varchar, p_keyword varchar,
    p_description varchar, p_category varchar, user_id uuid, 
    created_at timestamp, p_updated_at timestamp)
RETURNS integer AS $$
BEGIN
	UPDATE password
	SET
		url = p_url,
		username = p_username,
		keyword = p_keyword,
		description = p_description,
		category = p_category,
		updated_at = p_updated_at
	WHERE id = p_id;
	    -- Verificar si se realizó la actualización
    IF FOUND THEN
        RETURN 1; -- Actualización exitosa
    ELSE
        RETURN 0; -- Actualización fallida
    END IF;
END;
$$ LANGUAGE plpgsql;

------------- INSERCIONES y CONSULTAS -------------

-- busca todos los procedimientos con el nombre...
SELECT * FROM pg_proc WHERE proname = 'insert_user';

-- busca una rutina de nombre...
SELECT routine_name, routine_type, specific_name, data_type
FROM information_schema.routines
WHERE routine_name = 'update_password';

-- inserta un usuario
insert into users (id,email,pwd,created_at,updated_at)
values (uuid('15ed83be-01d5-4260-9677-1328c6083705'),'test@mail.com', 'testpwd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);