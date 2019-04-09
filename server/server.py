#!/usr/bin/python3
import requests
from flask import (
    Blueprint, redirect, request, session, url_for,
    jsonify, make_response
)
from server.domain.event import Event
from server.domain.user import User

bp = Blueprint('server', __name__, url_prefix='/')
bp.events = {}
bp.users = {}


@bp.errorhandler(404)
def error():
    return make_response(jsonify({'error': 'Not Found'}), 404)


@bp.route('/<string:username>/notifications', methods=['POST'])
def user_has_notifications(username):
    pass


@bp.route('/<string:username>/new_notifications', methods=['GET'])
def unread_notifications(username):
    pass


@bp.route('/<string:username>/old_notifications', methods=['GET'])
def readed_notifications(username):
    pass


@bp.route('/signin', methods=['POST'])
def new_user():
    try:
        content = request.get_json()
        bp.users[content['username']] = User(**content)
        print(bp.users[content['username']].to_str())
        return jsonify({"result": 200,
                        "message": "User " + content['username'] +
                        " created",
                        "user": bp.users[content['username']].to_dict()})
    except Exception as e:
        print(e.message)
        return jsonify({"result": 500, "message": e.message})


@bp.route('/login', methods=['POST'])
def login_user():
    try:
        content = request.get_json()
        if not bp.users[content['username']].logged:
            return jsonify({"result": 401,
                            "message": "User " + content['username'] +
                            " already logged"})
        if bp.users[content['username']].password == content['password']:
            bp.users[content['username']].logged = True
            return jsonify({"result": 200,
                            "message": "User " + content['username'] +
                            " accepted"})
        else:
            return jsonify({"result": 401,
                            "message": "User " + content['username'] +
                            " not accepted"})
    except Exception as e:
        print(e.message)
        return jsonify({"result": 500, "message": e.message})


@bp.route('/logout', methods=['POST'])
def logout_user():
    try:
        content = request.get_json()
        if bp.users[content['username']].logged:
            bp.users[content['username']].logged = False
            return jsonify({"result": 200,
                            "message": "User " + content['username'] +
                            " log out"})
        else:
            return jsonify({"result": 401,
                            "message": "User " + content['username'] +
                            " was not logged"})
    except Exception as e:
        print(e.message)
        return jsonify({"result": 500, "message": e.message})


@bp.route('/', methods=['GET'])
def list_events():
    return jsonify(bp.events)


@bp.route('/', methods=['POST'])
def publish_event():
    content = request.get_json()
    try:
        bp.events[content['name']] = Event(**content)
        print(bp.events[content['name']].to_str())
        return jsonify({"result": 200,
                        "message": "Event " + content['name'] + " create",
                        "event": bp.events[content['name']].to_dict()})
    except Exception as e:
        return jsonify({"result": 500, "message": e.message})


@bp.route('/<string:username>/topics/<string:topic>', methods=['POST'])
def add_interest_topic(username, topic):
    pass


def tests():
    print(requests.post())
    print(requests.get())


if __name__ == "__main__":

    tests()
