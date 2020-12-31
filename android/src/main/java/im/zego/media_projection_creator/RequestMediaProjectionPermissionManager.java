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

import im.zego.media_projection_creator.internal.MediaProjectionService;
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

    private int foregroundNotificationIcon = 0;

    private String foregroundNotificationText = "";

    private Context context;

    private Intent service;

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

    /// Developers need to set callback through manager in their native code to get media projection
    public void setRequestPermissionCallback(MediaProjectionCreatorCallback callback) {
        this.mediaProjectionCreatorCallback = callback;
    }

    /// Developers can set the foreground notification style (available since Android Q)
    public void setForegroundServiceNotificationStyle(int foregroundNotificationIcon, String foregroundNotificationText) {
        this.foregroundNotificationIcon = foregroundNotificationIcon;
        this.foregroundNotificationText = foregroundNotificationText;
    }



    /* ------- Private functions ------- */

    void requestMediaProjectionPermission(Context context, MethodChannel.Result result) {
        this.context = context;
        this.flutterResult = result;
        Intent intent = new Intent(context, RequestMediaProjectionPermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    void stopMediaProjectionService(Context context) {
        if (service != null) {
            context.stopService(service);
        }
    }

    public void onMediaProjectionCreated(MediaProjection mediaProjection, int errorCode) {
        this.invokeCallback(mediaProjection, errorCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createMediaProjection(int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            service = new Intent(this.context, MediaProjectionService.class);
            service.putExtra("code", resultCode);
            service.putExtra("data", intent);
            service.putExtra("notificationIcon", this.foregroundNotificationIcon);
            service.putExtra("notificationText", this.foregroundNotificationText);
            this.context.startForegroundService(service);
        } else {
            MediaProjectionManager manager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            MediaProjection mediaProjection = manager.getMediaProjection(resultCode, intent);
            this.onMediaProjectionCreated(mediaProjection, ERROR_CODE_SUCCEED);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }

        switch (action) {
            case "com.media_projection_creator.request_permission_result_succeeded":
                int resultCode = intent.getIntExtra("resultCode", 100);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.createMediaProjection(resultCode, intent);
                } else {
                    this.onMediaProjectionCreated(null, ERROR_CODE_FAILED_SYSTEM_VERSION_TOO_LOW);
                }
                break;
            case "com.media_projection_creator.request_permission_result_failed_user_canceled":
                invokeCallback(null, ERROR_CODE_FAILED_USER_CANCELED);
                break;
            case "com.media_projection_creator.request_permission_result_failed_system_version_too_low":
                invokeCallback(null, ERROR_CODE_FAILED_SYSTEM_VERSION_TOO_LOW);
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
