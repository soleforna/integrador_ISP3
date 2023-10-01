from datetime import timedelta
from flask_jwt_extended import create_access_token, decode_token


class JwtUtil:
    
    # Función para generar un token JWT
    @staticmethod
    def generate_jwt_token(user):
        """
        Genera un token JWT para el usuario dado.

        :param user: Objeto de usuario (por ejemplo, instancia de User)
        :return: Token JWT
        """
        # Puedes personalizar los datos que se almacenan en el token
        token_data = {
            'user_id': user.id,
            'email': user.email
        }

        # Genera el token JWT con los datos y el tiempo de expiración
        token = create_access_token(identity=token_data, expires_delta=timedelta(hours=1))

        return token


    @staticmethod
    def get_user_id_from_token(token):
        try:
            decoded_token = decode_token(token)
            user_id = decoded_token['identity']['user_id']
            return user_id
        except Exception as ex:
            # Maneja cualquier otra excepción que pueda ocurrir al decodificar el token
            raise ValueError("Error al decodificar el token: " + str(ex))
