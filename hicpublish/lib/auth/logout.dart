// import 'package:hicpublish/app.dart';
import 'package:hicpublish/main.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter/material.dart';
import 'package:hicpublish/controllers/auth_controller.dart';
// import 'package:shared_preferences/shared_preferences.dart';

Widget logoutButton(dynamic context){
  Widget result = SizedBox(
    height: 10,
    width: 60,
    child: RaisedButton(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(30.0)
      ),
      color: Colors.white,
      onPressed: () {
        SharedPreferences.getInstance().then((prefs){
          logoutUser(prefs.getString('username')).then((val){
            if (val) {
              // Navigator.pop(context);
              RestartWidget.restartApp(context);
            }
          });
        });
      },
      child: Icon(
        Icons.exit_to_app
      ),
    ),
  );
  SharedPreferences.getInstance().then((val) {
    result = val.getString('username')!=null||val.getString('username')!=""?result:null;
  });
  return result;
}
