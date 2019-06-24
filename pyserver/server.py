#!/usr/bin/python3
import requests
from flask import (
    Blueprint, request, jsonify, make_response
)
from pyserver.domain.event import Event
from pyserver.domain.user import User

from . import db

bp = Blueprint('server', __name__, url_prefix='/')
bp.events = {}
bp.users = {}


@bp.errorhandler(404)
def error():
    return make_response(jsonify({'error': 'Not Found'}), 404)


@bp.route('/signin', methods=['POST'])
def new_user():
    '''
    {
        "username": "",
        "name": "",
        "password": "",
        "logged": BOOL
    }
    '''
    try:
        content = request.get_json()
        bp.users[content['username']] = User(**content).to_dict()
        print(bp.users[content['username']])
        return jsonify({"result": 200,
                        "message": "User " + content['username'] +
                        " created",
                        "user": bp.users[content['username']]})
    except KeyError:
        return jsonify({"result": 404, "message": "User not registered"})
    except Exception as e:
        print(str(e))
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/login', methods=['POST'])
def login_user():
    '''
    {
        "username": "", 
        "password": "" 
    }
    '''
    try:
        content = request.get_json()
        if bp.users[content['username']]['logged']:
            return jsonify({"result": 401,
                            "message": "User " + content['username'] +
                            " already logged"})
        if bp.users[content['username']]['password'] == content['password']:
            bp.users[content['username']]['logged'] = True
            return jsonify({"result": 200,
                            "message": "User " + content['username'] +
                            " accepted"})
        else:
            return jsonify({"result": 401,
                            "message": "User " + content['username'] +
                            " not accepted"})
    except KeyError:
        return jsonify({"result": 404, "message": "User not registered"})
    except Exception as e:
        print(str(e))
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/<string:username>/logout', methods=['POST'])
def logout_user(username):
    try:
        if bp.users[username]['logged']:
            bp.users[username]['logged'] = False
            return jsonify({"result": 200,
                            "message": "User " + username +
                            " log out"})
        else:
            return jsonify({"result": 401,
                            "message": "User " + username +
                            " was not logged"})
    except KeyError:
        return jsonify({"result": 404, "message": "User not registered"})
    except Exception as e:
        print(str(e))
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/<string:username>', methods=["GET"])
def user_info(username):
    try:
        if bp.users[username]['logged']:
            return jsonify({"result": 200,
                            "message": "User " + username +
                            " informations",
                            "user": bp.users[username]})
        else:
            return jsonify({"result": 401,
                            "message": "User " + username +
                            " informations",
                            "user": bp.users[username]})
    except KeyError as e:
        return jsonify({"result": 404, "message": "User " +
                        str(e) + " not registered"})
    except Exception as e:
        print(str(e))
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/', methods=['GET'])
def list_events():
    return jsonify(list(bp.events.values()))


@bp.route('/', methods=['POST'])
def publish_event():
    '''
    {
        "name": "",
        "description": "",
        "author": "",
        "created": "",
        "end_date": ""
        "topics": []
    }
    '''
    content = request.get_json()
    try:
        bp.events[content['name']] = Event(**content).to_dict()
        print(str(bp.events[content['name']]))
        return jsonify({"result": 200,
                        "message": "Event " + content['name'] + " create",
                        "event": bp.events[content['name']]})
    except Exception as e:
        print(str(e))
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/<string:username>/topics/', methods=['POST'])
def add_interest_topics(username):
    content = request.get_json()

    try:
        if bp.users[username]['logged']:
            bp.users[username]['topics'].append(topic)
            return jsonify({"result": 201,
                            "message": "Topic " + topic + " added"})
        else:
            return jsonify({"result": 401,
                            "message": "User " + username +
                            " unauthorized"})
    except KeyError:
        return jsonify({"result": 404, "message": "User not registered"})
    except Exception as e:
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/<string:username>/topics/', mothod=['DELETE'])
def delete_interest_topics(username):
    '''
    {
        topics: []
    }
    '''
    content = request.get_json()


@bp.route('/topic/<string:topic>')
def get_event_by_topic(topic):
    topics = []
    for ev in bp.events:
        for t in bp.events.get(ev)['topics']:
            if t == topic:
                topics.append(bp.events.get(ev))
    return jsonify(topics)


@bp.route('/events/<int:id_event>/', mothod=['DELETE'])
def delete_event_topics(id_event):
    '''
    {
        topics: []
    }
    '''
    content = request.get_json()


@bp.route('/events/<int:id_event>/', mothod=['POST'])
def insert_event_topics(id_event):
    '''
    {
        topics: []
    }
    '''
    content = request.get_json()


def tests():
    print(requests.post())
    print(requests.get())


if __name__ == "__main__":

    tests()
