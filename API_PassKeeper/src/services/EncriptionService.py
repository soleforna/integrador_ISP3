import base64
import string
import secrets
from util.FernetFactory import FernetFactory

# Excepciones personalizadas para errores de encriptación y desencriptación
class EncryptionError(Exception):
    pass

class DecryptionError(Exception):
    pass

# Instancia de FernetFactory para crear un cifrador Fernet
factory = FernetFactory()
cipher_suite = factory.create_fernet_instance()

class EncryptionService:
    
    @staticmethod
    def encrypt_field(value):
        """
        Encripta un valor utilizando el cifrador Fernet.

        Args:
            value (str): El valor a encriptar.

        Returns:
            str: El valor encriptado en formato base64.
        
        Raises:
            EncryptionError: Si ocurre un error durante la encriptación.
        """
        try:
            encrypted_value = cipher_suite.encrypt(value.encode())
            return base64.b64encode(encrypted_value).decode()
        except Exception as ex:
            raise EncryptionError("Error de encriptación: " + str(ex))
    
    @staticmethod
    def decrypt_field(encrypted_value):
        """
        Desencripta un valor encriptado utilizando el cifrador Fernet.

        Args:
            encrypted_value (str): El valor encriptado en formato base64.

        Returns:
            str: El valor desencriptado.

        Raises:
            DecryptionError: Si ocurre un error durante la desencriptación.
        """
        try:
            decrypted_value = cipher_suite.decrypt(base64.b64decode(encrypted_value.encode()))
            return decrypted_value.decode()
        except Exception as ex:
            raise DecryptionError("Error de desencriptación: " + str(ex))

    @staticmethod
    def generateKey(long):
        characters = string.ascii_letters + string.digits + string.punctuation
        password = ''
        for _ in range(long):
            password+=secrets.choice(characters)
        return password
