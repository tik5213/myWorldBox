package com.MCWorld.module;

import com.MCWorld.data.profile.e.a;
import com.MCWorld.framework.base.http.io.Response.ErrorListener;
import com.MCWorld.framework.base.http.toolbox.error.VolleyError;
import com.MCWorld.framework.base.notification.EventNotifyCenter;

/* compiled from: StudioModule */
class z$38 implements ErrorListener {
    final /* synthetic */ int axB;
    final /* synthetic */ z axD;
    final /* synthetic */ a axE;

    z$38(z this$0, int i, a aVar) {
        this.axD = this$0;
        this.axB = i;
        this.axE = aVar;
    }

    public void onErrorResponse(VolleyError error) {
        EventNotifyCenter.notifyEvent(h.class, h.asx, new Object[]{Integer.valueOf(this.axB), Boolean.valueOf(false), null, this.axE});
    }
}
