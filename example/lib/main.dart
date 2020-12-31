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

  void finish() async {
    await MediaProjectionCreator.destroyMediaProjection();
    setState(() {
      createMediaProjectionResult = 'Unknown';
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
              SizedBox(height: 50),
              Text('Only support Android (5.0 or above)'),
              SizedBox(height: 20),
              CupertinoButton.filled(
                child: Text('Create MediaProjection'),
                onPressed: launch,
              ),
              SizedBox(height: 10),
              Text('Result: $createMediaProjectionResult'),

              SizedBox(height: 50),
              CupertinoButton.filled(
                child: Text('Destroy MediaProjection'),
                onPressed: finish,
              ),
            ],
          ),

        ),
      ),
    );
  }
}
