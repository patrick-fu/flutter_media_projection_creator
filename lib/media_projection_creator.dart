import 'dart:async';

import 'package:flutter/services.dart';

class MediaProjectionCreator {
  static const MethodChannel _channel =
      const MethodChannel('media_projection_creator');

  static const int ERROR_CODE_SUCCEED = 0;
  static const int ERROR_CODE_FAILED_USER_CANCELED = 1;
  static const int ERROR_CODE_FAILED_SYSTEM_VERSION_TOO_LOW = 2;

  /// Only support Android
  ///
  /// Developers need to set callback through `setRequestPermissionCallback` function
  /// in `im.zego.media_projection_creator.RequestMediaProjectionPermissionManager` class in their
  /// Android native code to get `android.media.projection.MediaProjection` instance.
  static Future<int> createMediaProjection() async {
    return await _channel.invokeMethod('createMediaProjection');
  }

  /// Only support Android
  /// Invoke this function will stop the media projection foreground notification service (since Android Q)
  static Future<int> destroyMediaProjection() async {
    return await _channel.invokeMethod('destroyMediaProjection');
  }
}
