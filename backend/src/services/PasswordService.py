from database.db import get_connection
from services.EncriptionService import EncryptionService
from entities.Password import Password

class PasswordService():
    
    @staticmethod 
    def encrypt_password_fields(pwd):
        pwd.url = EncryptionService.encrypt_field(pwd.url) 
        pwd.username = EncryptionService.encrypt_field(pwd.username) 
        pwd.keyword = EncryptionService.encrypt_field(pwd.keyword) 
        pwd.description = EncryptionService.encrypt_field(pwd.description) 
        pwd.category = EncryptionService.encrypt_field(pwd.category) 
        
    @classmethod
    def map_to_password(cls, row):
        """
        Mapea una fila de la base de datos a un objeto Password.
        """
        # Desencriptar los campos sensibles obtenidos de la base de datos
        url = EncryptionService.decrypt_field(row[1]) 
        username = EncryptionService.decrypt_field(row[2]) 
        keyword = EncryptionService.decrypt_field(row[3]) 
        description = EncryptionService.decrypt_field(row[4]) 
        category = EncryptionService.decrypt_field(row[5]) 
        user_id = row[6]
        password = Password(url, username, keyword, description, category, user_id)
        password.id = row[0]  # Configura el ID después de crear la instancia
        password.created_at = row[7]  # Establece la fecha de creación
        password.updated_at = row[8]  # Establece la fecha de actualización
        return password 
        
    @classmethod
    def get_password_byUserID(cls, user_id):
        """
        Obtiene todos los Password de un Usuario.
        """
        conn = get_connection() #Establecer una coneccion con la Base de Datos
        try:
            passwords = [] #Crear un array vacio
            with conn.cursor() as cursor: #Con la coneccion crear un cursor
                # Con el cursor llamar a la funcion de la BD que ejecuta la consulta
                cursor.execute("SELECT * FROM get_passwords_by_user_id(%s)", (user_id,))
                resulset = cursor.fetchall() #meter todos los resultados en un resulset
                cursor.close() # cerrar el cursor para que no apunte mas a la BD
                for row in resulset: # recorrer el resulset y por cada registro
                    password = cls.map_to_password(row) #mapear el registro a una entidad Password
                    passwords.append(password.to_JSON()) #Agregar la entidad al array passwords convertida en JSON
            return passwords #Retornar los passwords
        except Exception as ex:
            raise ex    
        finally:
            conn.close()#Cerrar la coneccion a la Base de Datos
            
    @classmethod
    def add_password(cls, pwd):
        """
        Agrega un nueva Password con los valores privados encriptados, de esta manera
        ni el administrador de la base de datos podra ver su contenido
        """
        conn = get_connection()
        try:
            with conn.cursor() as cursor:
                # Encriptar los campos antes de insertar en la base de datos
                cls.encrypt_password_fields(pwd)
                cursor.callproc(
                    'insert_password',
                    (
                        pwd.id, pwd.url, pwd.username, pwd.keyword, pwd.description,
                        pwd.category, pwd.user_id, pwd.created_at, pwd.updated_at
                    )
                )
                affected_rows = cursor.rowcount
                conn.commit()
            return affected_rows
        except Exception as ex:
            raise ex
        finally:
            conn.close()    
    
    @classmethod
    def get_all_passwords(cls):
        """
        Obtiene todos los Password, actualmente sin Uso
        """
        try:
            conn = get_connection()
            passwords = []
            with conn.cursor() as cursor:
                cursor.execute("SELECT id, url, username, keyword, description, category, user_id, created_at, updated_at FROM password")
                resulset = cursor.fetchall()
                cursor.close()
                for row in resulset:
                    password = cls.map_to_password(row)
                    passwords.append(password.to_JSON())
            conn.close()    
            return passwords
        except Exception as ex:
            raise ex
        
    @classmethod
    def get_password_byID(cls, id):
        """
        Obtiene un Password segun su ID
        """
        conn = get_connection()
        try:
            with conn.cursor() as cursor:
                cursor.execute("SELECT id, url, username, keyword, description, category, user_id, created_at, updated_at FROM password WHERE id = %s",(id,))
                row = cursor.fetchone()
                cursor.close()
                password = None
                if row is not None:
                    password = cls.map_to_password(row)
            return password
        except Exception as ex:
            raise ex    
        finally:
            conn.close()
            
    @classmethod
    def update_password(cls, pwd):
        conn = get_connection()
        try:
            with conn.cursor() as cursor:
                # Encriptar los campos antes de actualizar en la base de datos
                cls.encrypt_password_fields(pwd)
                cursor.callproc(
                    'update_password', 
                    (
                        pwd.id,
                        pwd.url,
                        pwd.username,
                        pwd.keyword,
                        pwd.description,
                        pwd.category,
                        pwd.user_id,
                        pwd.created_at,
                        pwd.updated_at
                    )
                )
                updated_row = cursor.fetchone()
                cursor.close()
                conn.commit()
                if updated_row and updated_row[0] == 1:
                    pwd = cls.map_to_password(pwd.to_tuple())
            return pwd.to_JSON()
        except Exception as ex:
            raise ex
        finally:
            conn.close()

    @classmethod
    def delete_password(cls, id):
        conn = get_connection()
        try:
            with conn.cursor() as cursor:
                cursor.execute("DELETE FROM password WHERE id = %s", (id,))
                conn.commit()
            return True  # Devuelve True si la eliminación fue exitosa
        except Exception as ex:
            raise ex
        finally:
            conn.close()
