import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:media_projection_creator/media_projection_creator.dart';

void main() {
  const MethodChannel channel = MethodChannel('media_projection_creator');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await MediaProjectionCreator.platformVersion, '42');
  });
}
