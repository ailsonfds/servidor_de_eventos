import 'package:hicpublish/controllers/post_controller.dart';
import 'package:hicpublish/models/user.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import 'dart:convert';

Future<bool> createUser(dynamic jsonMap) async {
  final String url = '$endpointServer/signin';

  final client = new http.Client();
  final streamedRest = await client.post(url, headers: {"Content-Type":"application/json"}, body: json.encode(jsonMap));
  print('Create user\n');
  print(streamedRest);
  final responseBody = json.decode(streamedRest.body);
  print(responseBody);

  return responseBody['result'] == 200;
}

Future<bool> loginUser(dynamic jsonMap) async {
  final String url = '$endpointServer/login';

  final client = new http.Client();
  final streamedRest = await client.post(url, headers: {"Content-Type":"application/json"}, body: json.encode(jsonMap));
  print('Login user\n');
  print(streamedRest.body);
  final responseBody = json.decode(streamedRest.body);
  print(responseBody);

  if (responseBody['result'] == 200) {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.setString("username", jsonMap['username']);
  }

  return false;
}

Future<bool> logoutUser(String username) async {
  final String url = '$endpointServer/user/$username/logout';

  final client = new http.Client();
  final streamedRest = await client.post(url);
  if (streamedRest.statusCode!=200) {
    return false;
  }
  final responseBody = json.decode(streamedRest.body);
  print(responseBody);

  if (responseBody['result'] == 200) {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.setString("username", "");
  }

  return false;
}

Future<User> getUser(String username) async {
  final String url = '$endpointServer/user/$username';

  final client = new http.Client();
  final streamedRest = await client.get(url);
  if (streamedRest.statusCode!=200) {
    return null;
  }
  final responseBody = json.decode(streamedRest.body);
  print(responseBody);
  return User.fromJson(responseBody);

}

Future<bool> checkUsername(String username) async {
  final String url = '$endpointServer/user/$username';

  final client = new http.Client();
  final streamedRest = await client.get(url);
  final responseBody = json.decode(streamedRest.body);
  return responseBody['result'] == 404;

}

Future<bool> checkUserLogged(String username) async {
  SharedPreferences prefs = await SharedPreferences.getInstance();
  User user = await getUser(username);
  return prefs.getString('username') == username && user.logged == true;
}