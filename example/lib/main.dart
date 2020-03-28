import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_folio_reader/flutter_folio_reader.dart';
import 'package:file_picker/file_picker.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool isWaiting = false;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Folio reader'),
        ),
        body: Center(
          child: RaisedButton(
            onPressed: () async {
              String path = await FilePicker.getFilePath(type: FileType.ANY);
              setState(() {
                isWaiting = true;
              });
              await FlutterFolioReader.openBook(path);
              setState(() {
                isWaiting = false;
              });
            },
            child: isWaiting ? CircularProgressIndicator() : Text('Open book'),
          ),
        ),
      ),
    );
  }
}
