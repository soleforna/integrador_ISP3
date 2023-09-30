from decouple import config

class Config: 
    SECRET_KEY= config('SECRET_KEY')
    JWT_SECRET_KEY = config('JWT_KEY')
    PORT= config('PORT')

class DevelopmentConfig(Config):
    DEBUG=False

config = {
    'development': DevelopmentConfig
}

