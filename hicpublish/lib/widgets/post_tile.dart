import 'package:flutter/material.dart';
import 'package:hicpublish/models/post.dart';
import 'package:hicpublish/screens/from_topic.dart';
import 'package:hicpublish/screens/post_view.dart';

class PostTile extends StatelessWidget {
  final Post _post;
  PostTile(this._post);

  @override
  Widget build(BuildContext context) => Column(
    children: <Widget>[
      ListTile(
        title: Text(
          _post.name,
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        subtitle: Row(
          mainAxisAlignment: MainAxisAlignment.start,
          children: topicsList(context, _post.topics)
        ),
        trailing: Icon(Icons.keyboard_arrow_right),
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => PostView(_post))
          );
        },
        selected: true,
      ),
      Divider()
    ],
  );
}

List<Widget> topicsList(context, List list){
  final children = <Widget>[];
  for (String c in list) {
    double l = c.length*9.0;
    double h = 25.0;
    children.add(
      SizedBox(
        width: l,
        height: h,
        child: RaisedButton(
          child: Text(c),
          padding: EdgeInsets.all(0),
          onPressed: () {
            Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => Topic(c)),
            );
          },
        ),
      ),
    );
  }
  return children;
}
