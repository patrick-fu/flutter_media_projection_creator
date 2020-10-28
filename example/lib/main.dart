import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

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

  void launch() async {
    int errorCode = await MediaProjectionCreator.createMediaProjection();

    setState(() {
      print('createMediaProjection, result: $errorCode');
      switch (errorCode) {
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
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('MediaProjection Creator example'),
        ),
        body: Center(
          child: Column(
            children: [
              Padding(padding: const EdgeInsets.only(top: 50.0)),
              Text('Only support Android (5.0 or above)'),
              Padding(padding: const EdgeInsets.only(top: 20.0)),
              CupertinoButton.filled(
                child: Text('Start Screen Capture'),
                onPressed: launch,
              ),
              Padding(padding: const EdgeInsets.only(top: 10.0)),
              Text('Result: $createMediaProjectionResult')
            ],
          ),

        ),
      ),
    );
  }
}
