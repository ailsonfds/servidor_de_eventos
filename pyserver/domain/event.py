import time
from flask import jsonify


class Event(object):
    """docstring for Event"""

    def __init__(self, name="", description="", author="", topics=[],
                 created=time.time(), end=""):
        super(Event, self).__init__()
        self.name = name
        self.description = description
        self.topics = topics
        self.author = author
        self.created = created
        self.end = time.mktime(time.strptime(end, "%Y-%m-%d"))

    def to_str(self):
        return "{" + self.name + "}," \
            "{" + self.description + "}," \
            "{" + self.topics + "}," \
            "{" + self.author + "}," \
            "{" + str(time.localtime(self.created)) + "}" \
            "{" + str(time.localtime(self.end)) + "}"

    def to_json(self):
        return jsonify(self.to_dict())

    def to_dict(self):
        return {"name": self.name,
                "description": self.description,
                "topics": self.topics,
                "author": self.author,
                "created": time.strftime("%Y-%m-%d",
                                         time.gmtime(self.created)),
                "end": time.strftime("%Y-%m-%d", time.gmtime(self.end))}
