import time
from flask import jsonify


class Event(object):
    """docstring for Event"""

    def __init__(self, name="", description="", author="",
                 created=time.clock()):
        super(Event, self).__init__()
        self.name = name
        self.description = description
        self.author = author
        self.created = created

    def to_str(self):
        return "{" + self.name + "}," \
            "{" + self.description + "}," \
            "{" + self.author + "}," \
            "{" + self.created + "}"

    def to_json(self):
        return jsonify(self.to_dict())

    def to_dict(self):
        return {"name": self.name,
                "description": self.description,
                "author": self.author,
                "created": self.created}
