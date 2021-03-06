from flask import jsonify


class User(object):
    """docstring for User"""

    def __init__(self, name="", username="", password="", topics=[]):
        super(User, self).__init__()
        self.name = name
        self.username = username
        self.password = password
        self.interest_topics = topics
        self.logged = False

    def to_str(self):
        return "{" + self.name + "}," \
            "{" + self.username + "}," \
            "{" + self.password + "}," \
            "{" + str(self.interest_topics) + "}," \
            "{" + str(self.logged) + "}"

    def to_json(self):
        return jsonify(self.to_dict())

    def to_dict(self):
        return {"name": self.name,
                "username": self.username,
                "password": self.password,
                "topics": self.interest_topics,
                "logged": self.logged}
