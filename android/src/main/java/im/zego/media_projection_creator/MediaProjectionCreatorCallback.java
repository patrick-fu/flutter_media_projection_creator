package im.zego.media_projection_creator;
//
//  MediaProjectionCreatorCallback.java
//  android
//  im.zego.media_projection_creator
//
//  Created by Patrick Fu on 2020/10/27.
//  Copyright © 2020 Zego. All rights reserved.
//

import android.media.projection.MediaProjection;

/// Developers need to set this callback through `setRequestPermissionCallback` function
/// in `RequestMediaProjectionPermissionManager` class in their native code to get MediaProjection instance.
public interface MediaProjectionCreatorCallback {

    /// When develops call dart's `createMediaProjection` function, it will call back the created `MediaProjection` object through this function
    /// @param errorCode The result of `createMediaProjection`
    void onMediaProjectionCreated(MediaProjection mediaProjection, int errorCode);
}
