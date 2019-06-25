
import 'package:flutter/material.dart';
import 'package:hicpublish/auth/login_view.dart';
import 'package:hicpublish/models/post.dart';
// import 'package:hicpublish/screens/from_topic.dart';
import 'package:hicpublish/screens/home.dart';
// import 'package:hicpublish/screens/new_post_view.dart';
// import 'package:hicpublish/screens/post_view.dart';

class PostListApp extends StatelessWidget {
  Post post;

  @override
  Widget build(BuildContext context) => MaterialApp(
    title: 'Post List App',
    debugShowCheckedModeBanner: false,
    theme: ThemeData(
      primaryColor: Colors.black,
      accentColor: Colors.black
    ),
    routes: <String,WidgetBuilder>{
      "/HomeScreen": (BuildContext context) => Home(),
      "/LoginScreen": (BuildContext context) => LoginScreen(),
    },
    home: Home(),
  );
}