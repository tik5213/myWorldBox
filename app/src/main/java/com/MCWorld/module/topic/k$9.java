package com.MCWorld.module.topic;

import com.MCWorld.framework.base.http.io.Response.ErrorListener;
import com.MCWorld.framework.base.http.toolbox.error.VolleyError;
import com.MCWorld.framework.base.log.HLog;
import com.MCWorld.framework.base.notification.EventNotifyCenter;
import com.MCWorld.module.h;

/* compiled from: TopicModule2 */
class k$9 implements ErrorListener {
    final /* synthetic */ k aCN;
    final /* synthetic */ int val$id;

    k$9(k this$0, int i) {
        this.aCN = this$0;
        this.val$id = i;
    }

    public void onErrorResponse(VolleyError error) {
        HLog.error("TopicModule2", "requestZoneContent error response " + error, new Object[0]);
        EventNotifyCenter.notifyEventUiThread(h.class, h.arP, new Object[]{Boolean.valueOf(false), null, Integer.valueOf(this.val$id)});
    }
}
