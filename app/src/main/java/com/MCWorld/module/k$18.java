package com.MCWorld.module;

import com.MCWorld.data.map.f;
import com.MCWorld.framework.base.http.io.Response.Listener;
import com.MCWorld.framework.base.json.Json;
import com.MCWorld.framework.base.log.HLog;
import com.MCWorld.framework.base.notification.EventNotifyCenter;

/* compiled from: MapModule */
class k$18 implements Listener<String> {
    final /* synthetic */ k atq;

    k$18(k this$0) {
        this.atq = this$0;
    }

    public void onResponse(String response) {
        try {
            f info = (f) Json.parseJsonObject(response, f.class);
            EventNotifyCenter.notifyEvent(n.class, 516, new Object[]{Boolean.valueOf(true), info});
        } catch (Exception e) {
            HLog.error(this, "requestMapCategory e = " + e + ", response = " + response, new Object[0]);
            EventNotifyCenter.notifyEvent(n.class, 516, new Object[]{Boolean.valueOf(false), null});
        }
    }
}
