package com.baidu.android.pushservice;

import android.content.Context;
import com.baidu.android.pushservice.apiproxy.BridgePushManager;

class PushManager$10 implements Runnable {
    final /* synthetic */ Context a;

    PushManager$10(Context context) {
        this.a = context;
    }

    public void run() {
        BridgePushManager.unbind(this.a);
    }
}
