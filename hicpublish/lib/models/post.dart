import 'package:intl/intl.dart';
import 'dart:ui';

class Post {
  final String name;
  final String description;
  final List<String> topics;
  final String author;
  final DateTime created;
  final DateTime end;

  Post.fromJson(Map<String, dynamic> jsonMap) :
    name = jsonMap["name"],
    description = jsonMap["description"],
    topics = jsonMap["topics"].cast<String>(),
    author = jsonMap["author"],
    created = DateTime.parse(jsonMap["created"]),
    end = DateTime.parse(jsonMap["end"]);
  
  dynamic toJson() => {
    'name': name,
    'description': description,
    'topics': topics,
    'author': author,
    'end': DateFormat('yyyy-MM-dd').format(end),
  };

  @override
  bool operator ==(Object other) =>
    identical(this, other) ||
    other is Post &&
    name == other.name;
  
  @override
  int get hashCode => hashValues(name, name);
}
