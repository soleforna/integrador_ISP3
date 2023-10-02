import uuid
from util.DateFormat import DateFormat 
from entities.Auditable import Auditable

class User(Auditable):
    def __init__(self, email, password):
        super().__init__()  # Llama al constructor de Auditable para inicializar las fechas
        self.id = str(uuid.uuid4())  # Genera un UUID aleatorio como ID
        self.email = email
        self.password = password

    
    def to_JSON(self):
        return {
            'id': self.id,
            'email': self.email,
            'password': self.password,  
            'created_at': DateFormat.convert_date(self.created_at), # Formatea la fecha de creación
            'updated_at': DateFormat.convert_date(self.updated_at)  # Formatea la fecha de actualización
        }
