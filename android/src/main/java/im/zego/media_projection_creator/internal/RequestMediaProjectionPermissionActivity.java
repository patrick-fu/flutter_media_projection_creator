package im.zego.media_projection_creator.internal;
//
//  RequestMediaProjectionPermissionActivity.java
//  android
//  im.zego.media_projection_creator
//
//  Created by Patrick Fu on 2020/10/27.
//  Copyright © 2020 Zego. All rights reserved.
//

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import im.zego.media_projection_creator.RequestMediaProjectionPermissionManager;

public class RequestMediaProjectionPermissionActivity extends Activity {

    private static final int RequestMediaProjectionPermissionCode = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.media_projection_creator.request_permission_result_succeeded");
        filter.addAction("com.media_projection_creator.request_permission_result_failed_user_canceled");
        filter.addAction("com.media_projection_creator.request_permission_result_failed_system_version_too_low");
        registerReceiver(RequestMediaProjectionPermissionManager.getInstance(), filter);

        if (Build.VERSION.SDK_INT < 21) {
            sendBroadcast(new Intent("com.media_projection_creator.request_permission_result_failed_system_version_too_low"));
            finish();
        } else {
            requestMediaProjectionPermission();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(RequestMediaProjectionPermissionManager.getInstance());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void requestMediaProjectionPermission() {
        MediaProjectionManager manager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(manager.createScreenCaptureIntent(), RequestMediaProjectionPermissionCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestMediaProjectionPermissionCode) {

            if (resultCode == RESULT_OK) {
                data.putExtra("resultCode", resultCode);
                data.setAction("com.media_projection_creator.request_permission_result_succeeded");
                sendBroadcast(data);
            } else {
                Intent intent = new Intent("com.media_projection_creator.request_permission_result_failed_user_canceled");
                intent.putExtra("resultCode", resultCode);
                sendBroadcast(intent);
            }

            finish();
        }
    }
}
