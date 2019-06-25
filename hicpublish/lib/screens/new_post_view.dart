import 'package:hicpublish/controllers/auth_controller.dart';
import 'package:hicpublish/models/user.dart';
import 'package:intl/intl.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:hicpublish/controllers/post_controller.dart';
import 'package:hicpublish/models/post.dart';
import 'package:shared_preferences/shared_preferences.dart';

class NewPostView extends StatefulWidget {
  final Post post;

  NewPostView(this.post);

  @override
  _NewPostViewState createState() => _NewPostViewState(post);
}

class _NewPostViewState extends State<NewPostView> {
  Post post;
  String name;
  String description;
  List<String> topics;
  String author;
  String created;
  String end;
  final _formKey = GlobalKey<FormState>();

  _NewPostViewState(this.post);

  @override 
  void initState() {
    super.initState();
  }

  sendForm() {
    setState(() {
      if (_formKey.currentState.validate()) {
        _formKey.currentState.save();
        String username;
        SharedPreferences.getInstance().then((val) => username = val.getString("username"));
        SharedPreferences.getInstance().whenComplete(() {
          User user;
          getUser(username).then((val) {
            user = val;
          }).whenComplete(() {
            Map<String, dynamic> jsonMap = <String,dynamic>{
              "created": DateFormat("yyyy-MM-dd").format(DateTime.now()),
              "topics": topics,
              "name": name,
              "description": description,
              "author": user.name,
              "end": end,
              "username": username,
            };
            print(jsonMap);
            post = Post.fromJson(jsonMap);
            insertPosts(post);
          });
        });
      }
    });
    if (_formKey.currentState.validate()) {
      Navigator.pop(context);
    }
  }

  final TextEditingController _controller = new TextEditingController();
  Future _chooseDate(BuildContext context, String initialDateString) async {
    var now = new DateTime.now();
    var initialDate = convertToDate(initialDateString) ?? now;
    initialDate = (initialDate.isAfter(now) ? initialDate : now);

    var result = await showDatePicker(
        context: context,
        initialDate: initialDate,
        firstDate: new DateTime.now().subtract(new Duration(days: 1)),
        lastDate: new DateTime(DateTime.now().year+50));

    if (result == null) return;

    setState(() {
      _controller.text = new DateFormat('dd/MM/yyyy').format(result);
      end = DateFormat("yyyy-MM-dd").format(result);
    });
  }

  DateTime convertToDate(String input) {
    try 
    {
      var d = new DateFormat('dd/MM/yyyy').parseStrict(input);
      return d;
    } catch (e) {
      return null;
    }    
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      centerTitle: true,
      title: Text('New Post'),
    ),
    body: Form (
      key: _formKey,
      child: Container(
        padding: EdgeInsets.all(30),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            Container(
              padding: EdgeInsets.all(20),
              child: TextFormField(
                decoration: new InputDecoration(
                  labelText: "Nome",
                  fillColor: Colors.white,
                  border: new OutlineInputBorder(
                    borderRadius: new BorderRadius.circular(25.0),
                    borderSide: new BorderSide(),
                  ),
                ),
                initialValue: post.name,
                validator: (val) {
                  if (val.length==0) {
                    return "O nome não pode ser vazio";
                  } else {
                    return null;
                  }
                },
                keyboardType: TextInputType.text,
                style: new TextStyle(
                  fontFamily: "Poppins",
                ),
                onSaved: (val) => setState(() => name = val),
              ),
            ),
            Container (
              padding: EdgeInsets.all(20),
              child: TextFormField(
                decoration: new InputDecoration(
                  labelText: "Descrição",
                  fillColor: Colors.white,
                  border: new OutlineInputBorder(
                    borderRadius: new BorderRadius.circular(25.0),
                    borderSide: new BorderSide(),
                  ),
                ),
                initialValue: post.description,
                keyboardType: TextInputType.multiline,
                style: new TextStyle(
                  fontFamily: "Poppins",
                ),
                onSaved: (val) => setState(() => description = val),
              ),
            ),
            Container (
              padding: EdgeInsets.all(20),
              child: TextFormField(
                decoration: new InputDecoration(
                  labelText: "Tópicos de Interesse Relacionado",
                  fillColor: Colors.white,
                  border: new OutlineInputBorder(
                    borderRadius: new BorderRadius.circular(25.0),
                    borderSide: new BorderSide(),
                  ),
                ),
                initialValue: post.topics.toString().replaceFirst("[","").replaceFirst("]", ""),
                validator: (val) {
                  if (val.length==0) {
                    return "Insira ao menos um tópico";
                  } else {
                    try {
                      val.split(RegExp(r'[\s,;/|]'));
                    } catch (e) {
                      return "Algo de errado $e";
                    }
                    return null;
                  }
                },
                keyboardType: TextInputType.text,
                style: new TextStyle(
                  fontFamily: "Poppins",
                ),
                onSaved: (val) => setState(() => topics = val.split(new RegExp(r'[\s,;/|]'))),
              ),
            ),
            Container (
              padding: EdgeInsets.all(20),
              child: Row(children: <Widget>[
                new Expanded(
                  child: new TextFormField(
                  decoration: new InputDecoration(
                    labelText: 'Dia do Evento',
                    fillColor: Colors.white,
                    border: new OutlineInputBorder(
                      borderRadius: new BorderRadius.circular(25.0),
                      borderSide: new BorderSide(),
                    ),
                  ),
                  controller: _controller,
                  keyboardType: TextInputType.datetime,
                  onSaved: (val) => setState(() => end = DateFormat("yyyy-MM-dd").format(DateFormat('dd/MM/yyyy').parse(val))),
                )),
                new IconButton(
                  icon: new Icon(Icons.calendar_today),
                  tooltip: 'Escolha uma data',
                  onPressed: (() {
                    _chooseDate(context, _controller.text);
                  }),
                )
              ]),
            ),
            RaisedButton (
              onPressed: sendForm,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(30.0)
              ),
              child: Text(
                  'Postar',
                  style: TextStyle(color: Colors.black),
              ),
            ),
          ],
        ),
      ),
    ),
  );
}
