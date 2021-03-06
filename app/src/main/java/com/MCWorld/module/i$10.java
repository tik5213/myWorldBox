package com.MCWorld.module;

import com.MCWorld.data.map.f;
import com.MCWorld.framework.base.http.io.Response.Listener;
import com.MCWorld.framework.base.json.Json;
import com.MCWorld.framework.base.log.HLog;
import com.MCWorld.framework.base.notification.EventNotifyCenter;

/* compiled from: JsModule */
class i$10 implements Listener<String> {
    final /* synthetic */ i ato;

    i$10(i this$0) {
        this.ato = this$0;
    }

    public void onResponse(String response) {
        try {
            HLog.verbose(this, "requestJSSearch response = " + response, new Object[0]);
            f info = (f) Json.parseJsonObject(response, f.class);
            EventNotifyCenter.notifyEvent(n.class, 1284, new Object[]{Boolean.valueOf(true), info});
        } catch (Exception e) {
            HLog.error(this, "requestJSSearch e = " + e + ", response = " + response, new Object[0]);
            EventNotifyCenter.notifyEvent(n.class, 1284, new Object[]{Boolean.valueOf(false), null});
        }
    }
}
