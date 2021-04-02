import 'package:flutter/material.dart';
import 'package:hicpublish/auth/logout.dart';
import 'package:shared_preferences/shared_preferences.dart';

AppBar postAppBar(context, String name) {
  AppBar appbar;
  bool trigger = true;
  SharedPreferences.getInstance().then((val) {
    if (val.getString("username") != "" || val.getString("username") != null) {
      appbar = AppBar(
        centerTitle: true,
        title: Text(
          name,
          style: TextStyle(
            fontWeight: FontWeight.bold,
            fontStyle: FontStyle.italic,
          ),
        ),
        actions: <Widget>[
          logoutButton(context),
        ],
      );
    }
    appbar = AppBar(
      centerTitle: true,
      title: Text(
        name,
        style: TextStyle(
          fontWeight: FontWeight.bold,
          fontStyle: FontStyle.italic,
        ),
      ),
    );
  }).whenComplete(() => trigger = false);
  while (trigger) {
    print("tá no loop");
  }
  return appbar;
}
