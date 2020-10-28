package im.zego.media_projection_creator;
//
//  RequestMediaProjectionPermissionManager.java
//  android
//  im.zego.media_projection_creator.internal
//
//  Created by Patrick Fu on 2020/10/27.
//  Copyright © 2020 Zego. All rights reserved.
//

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import im.zego.media_projection_creator.internal.RequestMediaProjectionPermissionActivity;
import io.flutter.Log;
import io.flutter.plugin.common.MethodChannel;

public class RequestMediaProjectionPermissionManager extends BroadcastReceiver {

    public static final int ERROR_CODE_SUCCEED = 0;
    public static final int ERROR_CODE_FAILED_USER_CANCELED = 1;
    public static final int ERROR_CODE_FAILED_SYSTEM_VERSION_TOO_LOW = 2;

    @SuppressLint("StaticFieldLeak")
    private static RequestMediaProjectionPermissionManager instance;

    private MediaProjectionCreatorCallback mediaProjectionCreatorCallback;

    private Context context;

    private MethodChannel.Result flutterResult;

    public static RequestMediaProjectionPermissionManager getInstance() {
        if (instance == null) {
            synchronized (RequestMediaProjectionPermissionManager.class) {
                if (instance == null) {
                    instance = new RequestMediaProjectionPermissionManager();
                }
            }
        }
        return instance;
    }

    /// Developers need to set callback through manager in their native code to get mediaprojection
    public void setRequestPermissionCallback(MediaProjectionCreatorCallback callback) {
        mediaProjectionCreatorCallback = callback;
    }


    /* ------- Private functions ------- */

    void requestMediaProjectPermission(Context context, MethodChannel.Result result) {
        this.context = context;
        this.flutterResult = result;
        Intent intent = new Intent(context, RequestMediaProjectionPermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private MediaProjection createMediaProjection(int resultCode, Intent intent) {
        MediaProjectionManager manager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        return manager.getMediaProjection(resultCode, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }

        int errorCode;

        switch (action) {
            case "com.media_projection_creator.request_permission_result_succeeded":
                int resultCode = intent.getIntExtra("resultCode", 100);
                MediaProjection mediaProjection = null;
                if (Build.VERSION.SDK_INT >= 21) {
                    mediaProjection = createMediaProjection(resultCode, intent);
                    errorCode = ERROR_CODE_SUCCEED;
                } else {
                    errorCode = ERROR_CODE_FAILED_SYSTEM_VERSION_TOO_LOW;
                }
                invokeCallback(mediaProjection, errorCode);
                break;
            case "com.media_projection_creator.request_permission_result_failed_user_canceled":
                errorCode = ERROR_CODE_FAILED_USER_CANCELED;
                invokeCallback(null, errorCode);
                break;
            case "com.media_projection_creator.request_permission_result_failed_system_version_too_low":
                errorCode = ERROR_CODE_FAILED_SYSTEM_VERSION_TOO_LOW;
                invokeCallback(null, errorCode);
                break;
        }
    }

    private void invokeCallback(MediaProjection mediaProjection, int errorCode) {
        if (mediaProjectionCreatorCallback != null) {
            mediaProjectionCreatorCallback.onMediaProjectionCreated(mediaProjection, errorCode);
        }

        Log.d("ZEGO", "[invokeCallback], errorCode " + errorCode);

        if (this.flutterResult != null) {
            Log.d("ZEGO", "[invokeCallback], flutter result, errorCode " + errorCode);
            this.flutterResult.success(errorCode);
        }
    }
}
