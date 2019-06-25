// import 'package:intl/intl.dart';
import 'dart:ui';

class User {
  // username | name | password | logged 
  final String username;
  final String name;
  final String password;
  bool logged;

  User.fromJson(Map<String, dynamic> jsonMap) :
    username = jsonMap['username'],
    name = jsonMap["name"],
    password = jsonMap["password"],
    logged = jsonMap["logged"]!=null?jsonMap['logged']:false;

  dynamic toJson() => {
    'username': username,
    'name': name,
    'password': password,
    'logged': logged,
  };

  @override
  bool operator ==(Object other) =>
    identical(this, other) ||
    other is User &&
    username == other.username;
  
  @override
  int get hashCode => hashValues(username, password);
}
