#!/usr/bin/python3
import requests
from flask import (
    Blueprint, request, jsonify, make_response
)
from pyserver.domain.event import Event
from pyserver.domain.user import User

from pyserver.db import db

bp = Blueprint('server', __name__, url_prefix='/')

@bp.errorhandler(404)
def error():
    return make_response(jsonify({'error': 'Not Found'}), 404)


@bp.route('/signin', methods=['POST'])
def new_user():
    '''
    {
        "username": "",
        "name": "",
        "password": ""
    }
    '''
    content = request.get_json()

    if 'username' in content and 'name' in content and 'password' in content:
        if db.username_already_exists(content['username']) == False:
            db.insert_user(content)
            
            return jsonify({"result": 200,
                            "message": "User " + content['username'] + " created successfully",
                            "user": content})
        else:
            return jsonify({"result": 401,
                            "message": "Username already exists"})
    
    return jsonify({"result": 400,
                    "message": "The request cannot be fulfilled due to bad syntax"})


@bp.route('/login', methods=['POST'])
def login_user():
    '''
    {
        "username": "", 
        "password": "" 
    }
    '''
    content = request.get_json()

    if 'username' in content and 'password' in content:
        if db.credentials_exists(content) == False:
            if db.user_already_logged(content['username']) == False:
                db.login_user(content['username'])
                
                return jsonify({"result": 200,
                                "message": "User " + content['username'] + " is now logged"})
            else:
                return jsonify({"result": 401,
                                "message": "User already logged"})    
        else:
            return jsonify({"result": 401,
                            "message": "Authentication failed"})
    
    return jsonify({"result": 400,
                    "message": "The request cannot be fulfilled due to bad syntax"})


@bp.route('/user/<string:username>/logout', methods=['POST'])
def logout_user(username):
    if db.username_already_exists(username) == True:
        if db.user_already_logged(username) == True:
            db.logout_user(username)
            
            return jsonify({"result": 200,
                            "message": "User " + username + " is now unlogged"})
        else:
            return jsonify({"result": 401,
                            "message": "User already unlogged"})
    else:
        return jsonify({"result": 404,
                        "message": "Username doesn't exists"})
    

@bp.route('/user/<string:username>', methods=["GET"])
def user_info(username):
    if db.username_already_exists(username) == True:
        return jsonify(db.get_user_info(username))
    
    return jsonify({"result": 404,
                    "message": "Username doesn't exists"})


@bp.route('/', methods=['GET'])
def list_events():
    return jsonify({"events":db.select_events()})


@bp.route('/topic/<string:topic>', methods=['GET'])
def get_event_by_topic(topic):
    return jsonify({"events":db.select_events_by_topic(topic)})


@bp.route('/', methods=['POST'])
def publish_event():
    '''
    {
        "name": "",
        "description": "",
        "author": "",
        "created": "",
        "end_date": "",
        "topics": []
    }
    '''
    content = request.get_json()

    if 'name' in content and 'description' in content and 'author' in content and 'created' in content and 'end_date' in content and 'topics' in content:
        if db.username_already_exists(content['username']) == True:
            db.insert_event(content)
            return jsonify({"result": 200,
                            "message": "Event " + content['name'] + " created successfully",
                            "event": content})
        else:
            return jsonify({"result": 404,
                            "message": "Username doesn't exists"})
        
    return jsonify({"result": 400,
                    "message": "The request cannot be fulfilled due to bad syntax"})


@bp.route('/user/<string:username>/topics', methods=['POST'])
def add_interest_topics(username):
    '''
    {
        topics: []
    }
    '''
    content = request.get_json()
    
    if 'topics' in content:
        if db.username_already_exists(username) == True:
            content['username'] = username
            db.insert_interest_topics(content)
            return jsonify({"result": 200,
                            "message": "New interest topics added successfully"})
        else:
            return jsonify({"result": 404,
                            "message": "Username doesn't exists"})

    return jsonify({"result": 400,
                    "message": "The request cannot be fulfilled due to bad syntax"})



@bp.route('/user/<string:username>/topics', methods=['DELETE'])
def delete_interest_topics(username):
    '''
    {
        topics: []
    }
    '''
    content = request.get_json()
    
    if 'topics' in content:
        if db.username_already_exists(username) == True:
            content['username'] = username
            db.remove_interest_topics(content)
            return jsonify({"result": 200,
                            "message": "New interest topics added successfully"})
        else:
            return jsonify({"result": 404,
                            "message": "Username doesn't exists"})

    return jsonify({"result": 400,
                    "message": "The request cannot be fulfilled due to bad syntax"})


@bp.route('/events/<int:id_event>', methods=['DELETE'])
def delete_event_topics(id_event):
    '''
    {
        topics: []
    }
    '''
    content = request.get_json()
    
    if 'topics' in content:
        if db.event_exists_by_id(id_event) == True:
            content['id_event'] = id_event
            db.delete_topics(content)
            
            return jsonify({"result": 200,
                            "message": "Topics deleted successfully"})
        else:
            return jsonify({"result": 404,
                            "message": "Event doesn't exists"})

    return jsonify({"result": 400,
                    "message": "The request cannot be fulfilled due to bad syntax"})


@bp.route('/events/<int:id_event>', methods=['POST'])
def insert_event_topics(id_event):
    '''
    {
        topics: []
    }
    '''
    content = request.get_json()
    
    if 'topics' in content:
        if db.event_exists_by_id(id_event) == True:
            content['id_event'] = id_event
            db.insert_topics(content)
            
            return jsonify({"result": 200,
                            "message": "Topics added successfully"})
        else:
            return jsonify({"result": 404,
                            "message": "Event doesn't exists"})

    return jsonify({"result": 400,
                    "message": "The request cannot be fulfilled due to bad syntax"})


def tests():
    print(requests.post())
    print(requests.get())


if __name__ == "__main__":

    tests()
