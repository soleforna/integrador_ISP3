from flask import Blueprint, jsonify, request
from flask_jwt_extended import get_jwt_identity, jwt_required


#Entidades
from entities.Password import Password
# Servicios
from services.PasswordService import PasswordService
from services.EncriptionService import EncryptionService

pwd_routes = Blueprint('password_blueprint', __name__)

@pwd_routes.route('/') #ruta para acceder
@jwt_required() #se requiere autenticacion por JWT
def get_all():
    try:
        #Obtener la identidad del usuario del token
        current_user = get_jwt_identity() 
        #Obtener todas las contraseñas del usuario dueño del token
        return jsonify(PasswordService.get_password_byUserID(current_user['user_id']))
    except Exception as ex:
        return jsonify({'error': str(ex)}),500

@pwd_routes.route('/add', methods = ['POST'])
@jwt_required()
def add_pwd():
    # Obtén los datos del cuerpo de la solicitud POST
    data = request.json
    #Si no se envia request
    if data:
        current_user = get_jwt_identity()
        # Crea una instancia de la clase Password con los datos proporcionados
        password = Password(data['url'], data['username'], data['keyword'], data['description'], data['category'], current_user['user_id'])
    else:
        return jsonify({'error': 'No valid data was provided in the application.'}), 400

    # Agrega la contraseña a la base de datos utilizando el modelo
    try:
        rows_affected = PasswordService.add_password(password)
        if rows_affected > 0:
            return jsonify({'message': 'Password added correctly'}), 201
    except Exception as ex:
        return jsonify({'error': str(ex)}), 500

@pwd_routes.route('/<id>', methods=['PATCH'])
@jwt_required()
def patch_password(id):
    
    try:
        data = request.json #obtener los datos a cambiar desde el request de la consulta
        if not data: #si no hay datos a cambiar
            return jsonify({'error': 'No valid data was provided in the application.'}), 400
        
        password = PasswordService.get_password_byID(id) #Obtener el registro por el ID
        current_user = get_jwt_identity()
        
        if not password: #Si no existe el Password
            return jsonify({'error': 'Password not found.'}), 404
        
        # Verificar si el usuario del token es diferente del usuario de la contraseña
        if current_user['user_id'] != password.user_id:
            return jsonify({'error': 'Unauthorized. Current user does not match the password owner.'}), 401
        
        # Actualiza los campos de la contraseña con los nuevos valores proporcionados
        if 'url' in data:
            password.url = data['url']
        if 'username' in data:
            password.username = data['username']
        if 'keyword' in data:
            password.keyword = data['keyword']
        if 'description' in data:
            password.description = data['description']
        if 'category' in data:
            password.category = data['category']    
        #Actualiza la fecha de actualizacion con el metodo de Auditable
        password.update_timestamps()
        # Guarda la contraseña actualizada en la base de datos
        return PasswordService.update_password(password)
    except Exception as ex:
        return jsonify({'error': str(ex)}),500

@pwd_routes.route('/<id>', methods=['DELETE'])
@jwt_required()
def delete_password(id):
    try:
        password = PasswordService.get_password_byID(id) #Obtener el registro por el ID
        current_user = get_jwt_identity()
        
        if not password: #Si no existe el Password
            return jsonify({'error': 'Password not found.'}), 404
        
        # Verificar si el usuario del token es diferente del usuario de la contraseña
        if current_user['user_id'] != password.user_id:
            return jsonify({'error': 'Unauthorized. Current user does not match the password owner.'}), 401
        
        if PasswordService.delete_password(id):
            return jsonify({'message': 'The record was deleted successfully.'}), 200
        
    except Exception as ex:
        return jsonify({'error': str(ex)}),500

@pwd_routes.route('/gen', methods=['GET'])
@pwd_routes.route('/gen/<int:long>', methods=['GET'])
def generate_password(long=12):
    # Si es menor o igual a uno, establecer la longitud predeterminada en 12
    if long <= 1:
            long = 12
    # Generar una contraseña con la longitud especificada
    password = EncryptionService.generateKey(long)
    # Devolver la contraseña generada como una respuesta JSON
    return jsonify({'password': password}), 200

