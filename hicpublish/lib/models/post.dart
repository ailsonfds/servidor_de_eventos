import 'package:intl/intl.dart';
import 'dart:ui';

final df = DateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

DateTime formatDate(String date){
  if (date != null && date != "") {
    try {
      final dt = df.parse(date);
      return dt;
    } catch (e) {
      print(e);
      final dt = DateFormat("yyyy-MM-dd").parse(date);
      return dt;
    }
  }
  return DateTime.now();
}

class Post {
  //  id_event | name | description | author | created | end_date 
  final String id;
  final String name;
  final String description;
  final List<String> topics;
  final String author;
  final DateTime created;
  final DateTime end;

  Post.fromJson(Map<String, dynamic> jsonMap) :
    id = jsonMap['id'],
    name = jsonMap["name"],
    description = jsonMap["description"],
    topics = jsonMap["topics"].cast<String>(),
    author = jsonMap["author"],
    created = formatDate(jsonMap["created"]),
    end = formatDate(jsonMap["end_date"]);

  dynamic toJson() => {
    'name': name,
    'description': description,
    'topics': topics,
    'author': author,
    'end': DateFormat('yyyy-MM-dd').format(end),
    'created': DateFormat('yyyy-MM-dd').format(created),
  };

  @override
  bool operator ==(Object other) =>
    identical(this, other) ||
    other is Post &&
    id == other.id;
  
  @override
  int get hashCode => hashValues(id, id);
}
