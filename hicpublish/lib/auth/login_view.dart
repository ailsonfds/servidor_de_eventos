// import 'dart:io';

import 'package:flutter/material.dart';
import 'package:hicpublish/auth/signin_view.dart';
import 'package:hicpublish/controllers/auth_controller.dart';

class LoginScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new LoginScreenState();
  }

}

class LoginScreenState extends State<LoginScreen> {
  String username;
  String password;
  final _formKey = GlobalKey<FormState>();
  @override
  void initState() {
    super.initState();
  }

  login() {
    setState(() {
      if (_formKey.currentState.validate()) {
        _formKey.currentState.save();
        Map<String, dynamic> jsonMap = <String,dynamic>{
          "username": username,
          "password": password,
        };
        loginUser(jsonMap).then((onValue) => onValue?Navigator.pop(context):null);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar:  AppBar(
        centerTitle: true,
        title: Text('Login'),
      ),
      body: Form (
        key: _formKey,
        child: Container(
          padding: EdgeInsets.all(30),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Container (
                padding: EdgeInsets.all(30),
                child: TextFormField(
                  decoration: new InputDecoration(
                    labelText: "Nome de usuário",
                    fillColor: Colors.white,
                    border: new OutlineInputBorder(
                      borderRadius: new BorderRadius.circular(25.0),
                      borderSide: new BorderSide(),
                    ),
                  ),
                  initialValue: username,
                  validator: (val) {
                    if (val.length == 0) {
                      return 'Digite um nome de usuário.';
                    } else {
                      return null;
                    }
                  },
                  keyboardType: TextInputType.text,
                  style: new TextStyle(
                    fontFamily: "Poppins",
                  ),
                  onSaved: (val) => setState(() => username = val),
                ),
              ),
              Container (
                padding: EdgeInsets.all(30),
                child: TextFormField(
                  decoration: new InputDecoration(
                    labelText: "Senha",
                    fillColor: Colors.white,
                    border: new OutlineInputBorder(
                      borderRadius: new BorderRadius.circular(25.0),
                      borderSide: new BorderSide(),
                    ),
                  ),
                  initialValue: password,
                  validator: (val) {
                    if (val.length==0) {
                      return "Digite uma senha";
                    } else {
                      return null;
                    }
                  },
                  obscureText: true,
                  keyboardType: TextInputType.text,
                  style: new TextStyle(
                    fontFamily: "Poppins",
                  ),
                  onSaved: (val) => setState(() => password = val),
                ),
              ),
              RaisedButton (
                onPressed: login,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30.0)
                ),
                color: Colors.blue,
                child: Text(
                    'Login',
                    style: TextStyle(color: Colors.white),
                ),
              ),
              RaisedButton (
                onPressed: () {
                  Navigator.push(context, MaterialPageRoute(builder: (context) => SigninScreen()));
                },
                color: Colors.red,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30.0)
                ),
                child: Text(
                    'Signin',
                    style: TextStyle(color: Colors.black),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}