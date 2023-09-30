from entities.Auditable import Auditable
import uuid
from util.DateFormat import DateFormat 

class Password(Auditable):
    def __init__(self, url, username, keyword, description, category, user_id):
        super().__init__()  # Llama al constructor de Auditable para inicializar las fechas
        self.id = str(uuid.uuid4())  # Genera un UUID aleatorio como ID
        self.url = url
        self.username = username
        self.keyword = keyword
        self.description = description
        self.category = category
        self.user_id = user_id  # Agregamos el campo user_id para la relación con usuarios


    def to_JSON(self):
        return {
            'id': self.id,
            'url': self.url,
            'username': self.username,
            'keyword': self.keyword,  
            'description': self.description,
            'category': self.category,
            'user_id': self.user_id,
            'created_at': DateFormat.convert_date(self.created_at), # Formatea la fecha de creación
            'updated_at': DateFormat.convert_date(self.updated_at)  # Formatea la fecha de actualización
        }

    def to_tuple(self):
        # Convierte los atributos del objeto en una tupla
        return (
            self.id,
            self.url,
            self.username,
            self.keyword,
            self.description,
            self.category,
            self.user_id,
            self.created_at,
            self.updated_at
        )
