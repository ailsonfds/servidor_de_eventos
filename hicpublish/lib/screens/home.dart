import 'package:flutter/material.dart';
import 'package:hicpublish/auth/login_view.dart';
import 'package:hicpublish/auth/logout.dart';
import 'package:hicpublish/controllers/post_controller.dart';
import 'package:hicpublish/models/post.dart';
import 'package:hicpublish/screens/new_post_view.dart';
import 'package:hicpublish/widgets/post_tile.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Home extends StatefulWidget {
  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  List<Post> _posts = <Post>[];

  @override
  void initState() {
    Future<SharedPreferences> prefs = SharedPreferences.getInstance();
    prefs.then((val) {
      if (val.getString('username') == null || val.getString('username') == ''){
        Navigator.push(context, MaterialPageRoute(builder: (context) => LoginScreen()));
      }
    });
    super.initState();
    listenForPosts();
  }

  void listenForPosts() async {
    final Stream<Post> stream = await getPosts();
    stream.listen((Post post) =>
      setState(() =>  _posts.add(post))
    );
  }

  Future<void> _refreshPosts() async
  {
    final Stream<Post> stream = await getPosts();
    stream.listen((Post post) =>
      setState(() {
        if(_posts.contains(post)){
          _posts.remove(post);
          _posts.add(post);
        } else {
          _posts.add(post);
        }
      })
    );
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      centerTitle: true,
      title: Text(
        'Posts',
        style: TextStyle(
          fontWeight: FontWeight.bold,
          fontStyle: FontStyle.italic,
        ),
      ),
      actions: <Widget>[
        logoutButton(context),
      ],
    ),
    body: Container (
      color: Colors.white,
      child: RefreshIndicator(
        child: ListView.builder(
          itemCount: _posts.length,
          itemBuilder: (context, index) => PostTile(_posts[index]),
        ),
        onRefresh: _refreshPosts,
      ),
    ),
    floatingActionButton: FloatingActionButton(
      child: Icon(Icons.add),
      onPressed: () {
        Post post;
        Map<String, dynamic> jsonMap = <String,dynamic>{
          "created": DateFormat("yyyy-MM-dd").format(DateTime.now()),
          "topics": [],
          "name": "",
          "description": "",
          "author": "",
          "end": DateFormat("yyyy-MM-dd").format(DateTime.now()),
        };
        post = Post.fromJson(jsonMap);
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => NewPostView(post)),
        );
      },
    ),
  );
}
