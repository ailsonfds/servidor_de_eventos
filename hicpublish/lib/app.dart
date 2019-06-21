
import 'package:flutter/material.dart';
import 'package:hicpublish/screens/home.dart';

class PostListApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) => MaterialApp(
    title: 'Post List App',
    debugShowCheckedModeBanner: false,
    theme: ThemeData(
      primaryColor: Colors.black,
      accentColor: Colors.black
    ),
    home: Home(),
  );
}