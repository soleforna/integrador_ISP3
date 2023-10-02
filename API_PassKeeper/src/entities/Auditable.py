from datetime import datetime

class Auditable:
    def __init__(self):
        self.created_at = datetime.now()
        self.updated_at = datetime.now()

    def update_timestamps(self):
        self.updated_at = datetime.now()
