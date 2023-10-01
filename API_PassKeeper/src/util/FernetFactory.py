from cryptography.fernet import Fernet
from decouple import config

class FernetFactory:
    def __init__(self):
        self.key = config('ENCRYPTION_KEY')

    def create_fernet_instance(self):
        try:
            if not self.key or not isinstance(self.key, str):
                raise ValueError("The ENCRYPTION_KEY environment variable is not set or is not a valid string.")
            return Fernet(self.key)
        except Exception as ex:
            raise ValueError(f"Error when creating the Fernet instance: {str(ex)}")
