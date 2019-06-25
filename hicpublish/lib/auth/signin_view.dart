import 'package:flutter/material.dart';
import 'package:hicpublish/controllers/auth_controller.dart';
import 'package:hicpublish/models/user.dart';

class SigninScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new SigninScreenState();
  }

}

class SigninScreenState extends State<SigninScreen> {
  String username;
  String name;
  String password;
  User _user;
  final _formKey = GlobalKey<FormState>();
  String usernameValidate;

  @override
  void initState() {
    super.initState();
  }

  signin() async {
    bool response = await checkUsername(username);
    setState(() {
      if (_formKey.currentState.validate()) {
        print(response.toString());
        if (!response) {
          usernameValidate = "Nome de usuário já existe!";
        } else {
          usernameValidate = null;
        }
        if (usernameValidate == null) {
          _formKey.currentState.save();
          Map<String, dynamic> jsonMap = <String,dynamic>{
            "username": username,
            "name": name,
            "password": password,
          };
          _user = User.fromJson(jsonMap);
          createUser(_user).then((val) => val?Navigator.pop(context):null);
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar:  AppBar(
        centerTitle: true,
        title: Text('Cadastro'),
      ),
      body: Form (
        key: _formKey,
        child: Container(
          padding: EdgeInsets.all(30),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Container(
                padding: EdgeInsets.all(30),
                child: TextFormField(
                  decoration: new InputDecoration(
                    labelText: "Nome",
                    fillColor: Colors.white,
                    border: new OutlineInputBorder(
                      borderRadius: new BorderRadius.circular(25.0),
                      borderSide: new BorderSide(),
                    ),
                  ),
                  initialValue: name,
                  validator: (val) {
                    if (val.length==0) {
                      return "Digite seu nome.";
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
                    }
                    return usernameValidate;
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
                onPressed: signin,
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