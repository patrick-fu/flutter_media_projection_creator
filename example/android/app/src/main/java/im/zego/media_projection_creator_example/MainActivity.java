package im.zego.media_projection_creator_example;

import android.media.projection.MediaProjection;
import android.os.Bundle;

import androidx.annotation.Nullable;

import im.zego.media_projection_creator.MediaProjectionCreatorCallback;
import im.zego.media_projection_creator.RequestMediaProjectionPermissionManager;
import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends FlutterActivity {

    private static MediaProjection mediaProjection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /// Example: developers should call this method to set callback,
        /// when dart call `createMediaProjection`, it would be return a MediaProjection through this callback
        RequestMediaProjectionPermissionManager.getInstance().setRequestPermissionCallback(mediaProjectionCreatorCallback);
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
