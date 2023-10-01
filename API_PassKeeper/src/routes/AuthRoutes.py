# Importamos las clases y funciones necesarias
from flask import Blueprint, jsonify, request
from flask_jwt_extended import get_jwt_identity, jwt_required
from util.JwtUtil import JwtUtil

#Entidades
from entities.User import User
# Modelos
from services.UserService import UserService

# Creamos el blueprint para las rutas de autenticación
auth_routes = Blueprint('auth_blueprint', __name__)

@auth_routes.route('/register', methods=['POST'])
def create_user():
    # Obtenemos los datos del usuario desde la solicitud
    data = request.json
    # Verificamos si se proporcionaron datos válidos en la solicitud
    if not data:
        return jsonify({'error': 'No se proporcionaron datos válidos en la solicitud'}), 400
    else:
        # Creamos una instancia de la clase User con los datos proporcionados
        user = User(data['email'], data['password'])
    # Intentamos registrar al usuario
    try:
        user_add = UserService.sing_up(user)
        return jsonify({'token': JwtUtil.generate_jwt_token(user_add)}), 201
    except Exception as ex:
        return jsonify({'error': str(ex)}), 500

@auth_routes.route('/login', methods=['POST'])
def login():
    try:
        data = request.json
        if not data:
            return jsonify({'error': 'No valid data was provided in the application.'}), 400
        
        user = UserService.get_user_by_email(data['email'])
        
        if user is None:
            return jsonify({'error': 'Email not found'}), 401
        
        if data['password'] != user.password:
                return jsonify({'error': 'Incorrect password'}), 401
        else:
            token = JwtUtil.generate_jwt_token(user)
            return jsonify({'token': token}), 200
    except Exception as ex:
        return jsonify({'error': str(ex)}), 500

@auth_routes.route('/update', methods=['POST'])
@jwt_required() #se requiere autenticacion por JWT
def change_credentiasl():
    try:
        data = request.json
        if not data or not data['password']:
            return jsonify({'error': 'No valid data was provided in the application.'}), 400
        #Obtener la identidad del usuario del token
        current_user = get_jwt_identity()
        if UserService.update_user_password(current_user['user_id'], data['password']):
            return jsonify({'message': 'Updated credentials'}),200
    except Exception as ex:
        print('An exception occurred:', str(ex))
        return jsonify({'error': str(ex)}), 500
