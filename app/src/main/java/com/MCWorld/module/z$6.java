package com.MCWorld.module;

import com.MCWorld.data.studio.a;
import com.MCWorld.framework.base.http.io.Response.Listener;
import com.MCWorld.framework.base.notification.EventNotifyCenter;

/* compiled from: StudioModule */
class z$6 implements Listener<a> {
    final /* synthetic */ int axz;

    z$6(int i) {
        this.axz = i;
    }

    public /* synthetic */ void onResponse(Object obj) {
        a((a) obj);
    }

    public void a(a response) {
        if (response == null || !response.isSucc()) {
            EventNotifyCenter.notifyEvent(h.class, 792, new Object[]{Integer.valueOf(this.axz), Boolean.valueOf(false), response});
            return;
        }
        EventNotifyCenter.notifyEvent(h.class, 792, new Object[]{Integer.valueOf(this.axz), Boolean.valueOf(true), response});
    }
}
