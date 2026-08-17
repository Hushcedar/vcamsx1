package com.blackbox.vcam.model;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({VideoSourceType.FILE, VideoSourceType.RTMP, VideoSourceType.RTSP})
@Retention(RetentionPolicy.SOURCE)
public @interface VideoSourceType {
    int FILE = 0;
    int RTMP = 1;
    int RTSP = 2;
}
