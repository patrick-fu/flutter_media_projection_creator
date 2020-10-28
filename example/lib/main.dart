import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:media_projection_creator/media_projection_creator.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String createMediaProjectionResult = 'Unknown';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              CupertinoButton.filled(
                child: Text('Start Screen Capture'),
                onPressed: () {
                  MediaProjectionCreator.createMediaProjection().then((value) {
                    setState(() {
                      print('createMediaProjection, result: $value');
                      switch (value) {
                        case MediaProjectionCreator.ERROR_CODE_SUCCEED:
                          createMediaProjectionResult = 'Succeed';
                          break;
                        case MediaProjectionCreator.ERROR_CODE_FAILED_USER_CANCELED:
                          createMediaProjectionResult = 'Failed: User Canceled';
                          break;
                        case MediaProjectionCreator.ERROR_CODE_FAILED_SYSTEM_VERSION_TOO_LOW:
                          createMediaProjectionResult = 'Failed: System API level need to higher than 21';
                          break;
                      }
                    });
                  });
                }
              ),
              Text('Result: $createMediaProjectionResult')
            ],
          ),

        ),
      ),
    );
  }
}
