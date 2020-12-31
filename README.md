# MediaProjection Creator

[![pub package](https://img.shields.io/pub/v/media_projection_creator.svg)](https://pub.dev/packages/media_projection_creator)

A flutter plugin of the creator used to create `MediaProjection` instance (with requesting permission) for Android

> Note: Only support Android

## Related projects

### iOS

If you need to implement screen capture on **iOS**, I have also developed two helpful plugins:

1. **[ReplayKit Launcher](https://pub.dev/packages/replay_kit_launcher)**: A flutter plugin of the launcher used to open `RPSystemBroadcastPickerView` for iOS

2. **[Shared preferences with App Group](https://pub.dev/packages/shared_preference_app_group)**: Shared preference supporting iOS App Group capability (using `-[NSUserDefaults initWithSuiteName:]`)

## Usage

To use this plugin, add `media_projection_creator` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/).

### Example

- Flutter

```dart
int errorCode = await MediaProjectionCreator.createMediaProjection();
if (errorCode == MediaProjectionCreator.ERROR_CODE_SUCCEED) {
    print('createMediaProjection succeed');
}
```

- Android

```java
public class MainActivity extends FlutterActivity {

    private static MediaProjection mediaProjection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /// Example: developers should call this method to set callback,
        /// when dart call `createMediaProjection`, it would be return a MediaProjection through this callback
        RequestMediaProjectionPermissionManager.getInstance().setRequestPermissionCallback(mediaProjectionCreatorCallback);

        /// Customize the media projection foreground notification style (available since Android Q)
        /// If not set, it will be the system default style
        RequestMediaProjectionPermissionManager.getInstance().setForegroundServiceNotificationStyle(R.mipmap.ic_launcher, "Screen is being captured");
    }

    private final MediaProjectionCreatorCallback mediaProjectionCreatorCallback = new MediaProjectionCreatorCallback() {

        @Override
        public void onMediaProjectionCreated(MediaProjection projection, int errorCode) {
            if (errorCode == RequestMediaProjectionPermissionManager.ERROR_CODE_SUCCEED) {
                Log.i("MEDIA_PROJECTION_CREATOR", "Create media projection succeeded!");
                mediaProjection = projection;
            } else if (errorCode == RequestMediaProjectionPermissionManager.ERROR_CODE_FAILED_USER_CANCELED) {
                Log.e("MEDIA_PROJECTION_CREATOR", "Create media projection failed because can not get permission");
            } else if (errorCode == RequestMediaProjectionPermissionManager.ERROR_CODE_FAILED_SYSTEM_VERSION_TOO_LOW) {
                Log.e("MEDIA_PROJECTION_CREATOR", "Create media projection failed because system api level is lower than 21");
            }
        }
    };
}
```

Please see the example app of this plugin for a full example.

### Another practical demo

**[https://github.com/zegoim/zego-express-example-screen-capture-flutter](https://github.com/zegoim/zego-express-example-screen-capture-flutter)**

This demo implements screen live broadcast on iOS/Android by using the **[ZEGO Express Audio and Video Flutter SDK](https://pub.dev/packages/zego_express_engine)**

## Contributing

Everyone is welcome to contribute code via pull requests, to help people asking for help, to add to our documentation, or to help out in any other way.
