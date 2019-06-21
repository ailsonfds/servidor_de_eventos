import 'package:intl/intl.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:hicpublish/controllers/post_controller.dart';
import 'package:hicpublish/models/post.dart';

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
        Map<String, dynamic> jsonMap = <String,dynamic>{
          "created": DateFormat("yyyy-MM-dd").format(DateTime.now()),
          "topics": topics,
          "name": name,
          "description": description,
          "author": author,
          "end": end,
        };
        post = Post.fromJson(jsonMap);
        insertPosts(post);
      }
    });
    if (_formKey.currentState.validate()) {
      Navigator.pop(context);
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
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            Container(
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
              child: TextFormField(
                decoration: new InputDecoration(
                  labelText: "Autor",
                  fillColor: Colors.white,
                  border: new OutlineInputBorder(
                    borderRadius: new BorderRadius.circular(25.0),
                    borderSide: new BorderSide(),
                  ),
                ),
                initialValue: post.author,
                validator: (val) {
                  if (val.length==0) {
                    return "O author não pode ser vazio";
                  } else {
                    return null;
                  }
                },
                keyboardType: TextInputType.text,
                style: new TextStyle(
                  fontFamily: "Poppins",
                ),
                onSaved: (val) => setState(() => author = val),
              ),
            ),
            Container (
              child: TextFormField(
                decoration: new InputDecoration(
                  labelText: "Dia do Evento",
                  fillColor: Colors.white,
                  border: new OutlineInputBorder(
                    borderRadius: new BorderRadius.circular(25.0),
                    borderSide: new BorderSide(),
                  ),
                ),
                initialValue: DateFormat('dd/MM/yyyy').format(post.end),
                validator: (val) {
                  if (val.isEmpty) {
                    return "Inisira o dia do evento";
                  } else {
                    try {
                      if (DateTime.now().isAfter(DateFormat('dd/MM/yyyy').parse(val))) {
                        return "Esse dia já passou";
                      }
                    } catch (e) {
                      return "Formato inválido. Tente dia/mês/ano";
                    }
                  }
                },
                keyboardType: TextInputType.text,
                style: new TextStyle(
                  fontFamily: "Poppins",
                ),
                onSaved: (val) => setState(() => end = DateFormat("yyyy-MM-dd").format(DateFormat('dd/MM/yyyy').parse(val))),
              ),
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
