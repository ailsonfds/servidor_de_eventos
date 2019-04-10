#!/usr/bin/python3
import requests
from flask import (
    Blueprint, request, jsonify, make_response
)
from pyserver.domain.event import Event
from pyserver.domain.user import User

bp = Blueprint('server', __name__, url_prefix='/')
bp.events = {}
bp.users = {}


@bp.errorhandler(404)
def error():
    return make_response(jsonify({'error': 'Not Found'}), 404)


@bp.route('/<string:username>/notifications', methods=['POST'])
def user_has_notifications(username):
    try:
        if bp.users[username]['logged']:
            return jsonify({"result": 200,
                            "notifications":
                            bp.users[username].notifications[True].length()})
        else:
            return jsonify({"result": 401,
                            "message": "User " + username +
                            " unauthorized"})
    except KeyError:
        return jsonify({"result": 404, "message": "User not registered"})
    except Exception as e:
        print(str(e))
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/<string:username>/new_notifications', methods=['GET'])
def unread_notifications(username):
    try:
        if bp.users[username]['logged']:
            return jsonify({"result": 200,
                            "notifications":
                            bp.users[username].notifications[True]})
        else:
            return jsonify({"result": 401,
                            "message": "User " + username +
                            " unauthorized"})
    except KeyError:
        return jsonify({"result": 404, "message": "User not registered"})
    except Exception as e:
        print(str(e))
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/<string:username>/old_notifications', methods=['GET'])
def readed_notifications(username):
    try:
        if bp.users[username]['logged']:
            return jsonify({"result": 200,
                            "notifications":
                            bp.users[username].notifications[False]})
        else:
            return jsonify({"result": 401,
                            "message": "User " + username +
                            " unauthorized"})
    except KeyError:
        return jsonify({"result": 404, "message": "User not registered"})
    except Exception as e:
        print(str(e))
        return jsonify({"result": 500, "message": str(e)})


@bp.route('/signin', methods=['POST'])
def new_user():
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


@bp.route('/<string:username>/topics/<string:topic>', methods=['POST'])
def add_interest_topic(username, topic):
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


@bp.route('/topic/<string:topic>')
def get_event_by_topic(topic):
    topics = []
    for ev in bp.events:
        for t in bp.events.get(ev)['topics']:
            if t == topic:
                topics.append(bp.events.get(ev))
    return jsonify(topics)


def tests():
    print(requests.post())
    print(requests.get())


if __name__ == "__main__":

    tests()
