from flask import jsonify
from database.db import get_connection
from services.EncriptionService import EncryptionService
from entities.User import User

class UserService():
    
    @classmethod
    def map_to_user(cls, row):
        """
        Mapea una fila de la base de datos a un objeto User.
        """
        # Desencriptar los campos sensibles obtenidos de la base de datos
        email = row[1] 
        password = EncryptionService.decrypt_field(row[2]) 
        user = User(email, password)
        user.id = row[0]  # Configura el ID después de crear la instancia
        user.created_at = row[3]  # Establece la fecha de creación
        user.updated_at = row[4]  # Establece la fecha de actualización
        return user 
    
    @classmethod
    def sing_up(cls, user):
        conn = get_connection()
        try:
            with conn.cursor() as cursor:
                # Encriptar la contraseña antes de insertar en la base de datos
                user.password = EncryptionService.encrypt_field(user.password)
                # LLamar a un procedimiento almacenado en la Base de Datos
                cursor.callproc(
                    'insert_user',
                        (
                            user.id, user.email, user.password,
                            user.created_at, user.updated_at
                        )
                )
                
                if cursor.rowcount == 0:
                    return jsonify({'error': 'User could not be created'}), 409
                else:
                    conn.commit()
            return user
        except Exception as ex:
            raise ex  
        finally:
            conn.close()    
            
    @classmethod
    def get_user_by_email(cls, email):
        conn = get_connection()
        try:
            with conn.cursor() as cursor:
                cursor.execute("SELECT id, email, pwd, created_at, updated_at FROM users WHERE email = %s", (email,))
                user_data = cursor.fetchone()
                if user_data:
                    user = cls.map_to_user(user_data)
                    return user
            return None 
        except Exception as ex:
            raise ex
        finally:
            conn.close()
            
    @classmethod
    def update_user_password(cls, id, pwd):
        try:
            conn = get_connection()
            # Encriptar la contraseña antes de insertar en la base de datos
            password = EncryptionService.encrypt_field(pwd)
            with conn.cursor() as cursor:
                cursor.execute(
                "UPDATE users SET pwd = %s WHERE id = %s",
                (password, id)
                )
                if cursor.rowcount > 0:
                    conn.commit()# Guardar los cambios si hubo actualización
                    conn.close()
                    return True
                else:
                    conn.close()
                    return False  # No se actualizó ningún registro
        except Exception as ex:
            raise ex
