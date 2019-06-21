import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:hicpublish/screens/new_post_view.dart';
import 'package:intl/intl.dart';
import 'package:hicpublish/models/post.dart';
import 'package:hicpublish/widgets/post_tile.dart';
import 'package:hicpublish/controllers/post_controller.dart';

class PostView extends StatefulWidget {
  final Post post;
  PostView(this.post);

  @override
  _PostViewState createState() => _PostViewState(post);
}

class _PostViewState extends State<PostView> {
  Post post;

  _PostViewState(this.post);

  @override
  void initState() {
    super.initState();
  }

  Future<void> _refreshPost() async
  {
    final Stream<Post> stream = await getPosts();

    stream.listen((Post p) =>
      setState(() {
        if(post==p){
          post = p;
        }
      })
    );
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      centerTitle: true,
      title: Text(
        post.name,
        style: TextStyle(
          fontWeight: FontWeight.bold,
          fontStyle: FontStyle.italic,
        ),
      ),
      actions: <Widget>[
        SizedBox(
          height: 10,
          width: 60,
          child: RaisedButton(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(30.0)
            ),
            color: Colors.white,
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => NewPostView(post))
              );
            },
            child: Icon(
              Icons.edit
            ),
          ),
        ),
        SizedBox(
          height: 10,
          width: 60,
          child: RaisedButton(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(30.0)
            ),
            color: Colors.white,
            onPressed: () {
              Navigator.pop(context);
            },
            child: Icon(
              Icons.delete
            ),
          ),
        ),
      ],
    ),
    body: Container(
      color: Colors.black,
      alignment: Alignment.topLeft,
      child: RefreshIndicator(
        onRefresh: _refreshPost,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            _FrameField(
              Row(
                children: <Widget>[
                  _FrameField (
                    Text(
                      'Tópicos:',
                      style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                    )
                  ),
                  _FrameField(
                    Card(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: topicsList(context, post.topics),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            _FrameField(
              Row (
                children: <Widget>[
                  _FrameField(
                    Text(
                      'Dia do evento:',
                      textAlign: TextAlign.left,
                      style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                    ),
                  ),
                  _FrameField(
                    Text(
                      DateFormat('dd/MM/yyyy').format(post.end),
                      textAlign: TextAlign.left,
                      style: TextStyle(color: Colors.white),
                    ),
                  ),
                ],
              ),
            ),
            _FrameField(
              Row(
                children: <Widget>[
                  _FrameField (
                    Text(
                      'Autor:',
                      style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                    )
                  ),
                  _FrameField(
                    Text(
                      post.author,
                      style: TextStyle(color: Colors.white),
                    ),
                  ),
                ],
              ),
            ),
            _FrameField(
              Row(
                children: <Widget>[
                  _FrameField (
                    Text(
                      'Descrição:',
                      style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                    ),
                  ),
                ],
              ),
            ),
            _FrameField (
              _FrameTextArea (
                Text(
                  post.description,
                  textAlign: TextAlign.left,
                ),
              ),
            ),
          ],
        ),
      ),
    ),
  );
}

class _FrameField extends StatelessWidget {
  final Widget widget;
  
  _FrameField(this.widget);
  
  @override
  Widget build(BuildContext context) => Container (
    decoration: new BoxDecoration(
      shape: BoxShape.rectangle,
      border: Border.all(
        width: 5,
      ),
      color: Colors.black,
    ),
    child: widget,
    padding: EdgeInsets.all(0),
    margin: EdgeInsets.all(0),
  );
}

class _FrameTextArea extends StatelessWidget {
  final Widget widget;
  
  _FrameTextArea(this.widget);
  
  @override
  Widget build(BuildContext context) => Container (
    decoration: new BoxDecoration(
      shape: BoxShape.rectangle,
      border: Border.all(
        width: 5,
      ),
      color: Colors.white,
    ),
    child: SizedBox(
      height: MediaQuery.of(context).size.height*0.6,
      width: MediaQuery.of(context).size.width,
      child: widget,
    ),
    padding: EdgeInsets.fromLTRB(10,0,10,0),
    margin: EdgeInsets.all(0),
  );
}
