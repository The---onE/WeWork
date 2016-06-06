package com.xmx.wework.Tools.Data.Sync;

import com.xmx.wework.Tools.Data.Cloud.ICloudEntity;
import com.xmx.wework.Tools.Data.SQL.ISQLEntity;

/**
 * Created by The_onE on 2016/5/29.
 */
public interface SyncEntity extends ICloudEntity, ISQLEntity {
    public String getCloudId();
    public void setCloudId(String id);
}
