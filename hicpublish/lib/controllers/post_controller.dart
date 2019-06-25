// import 'dart:collection';

import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'dart:convert';
import 'package:hicpublish/models/post.dart';

final String endpointServer = 'https://hicpublish.herokuapp.com';

Future<Stream<Post>> getPosts() async {
  final String url = '$endpointServer/';

  final client = new http.Client();
  final streamedRest = await client.send(
    http.Request('get', Uri.parse(url))
  );

  return streamedRest.stream
     .transform(utf8.decoder)
     .transform(json.decoder)
     .expand((data) => (data as List))
     .map((data) => Post.fromJson(data));
}

Future<Stream<Post>> getPostsFromTopic(String topic) async {
  final String url = '$endpointServer/topic/$topic';

  final client = new http.Client();
  final streamedRest = await client.send(
    http.Request('get', Uri.parse(url))
  );

  return streamedRest.stream
     .transform(utf8.decoder)
     .transform(json.decoder)
     .expand((data) => (data as List))
     .map((data) => Post.fromJson(data));
}

Future<Response> insertPosts(Post post) async {
  final String url = '$endpointServer/';

  final client = new http.Client();
  Map<String,String> head = {
    'Content-Type': 'application/json',
  };
  String body = json.encode(post);

  final streamRest = await client.post(
      Uri.parse(url),
      headers: head,
      body: body,
  );

  print(streamRest.body);

  return streamRest;

}

Future<Response> deletePosts(int id) async {
  final String url = '$endpointServer/events/$id';

  final client = new http.Client();

  final streamRest = await client.delete(
      Uri.parse(url),
  );
  print(url);
  print(streamRest.body);

  return streamRest;

}

Future<Post> getPost(String id) async {
  final String url = '$endpointServer/$id';

  final client = new http.Client();
  final streamedRest = await client.get(
    Uri.parse(url)
  );

  Post post;
  print(json.decode(streamedRest.body));
  post = Post.fromJson(json.decode(streamedRest.body));

  return post;
}

