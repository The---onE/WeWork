package com.xmx.wework.Calendar;

import android.content.ContentValues;
import android.database.Cursor;

import com.avos.avoscloud.AVObject;
import com.xmx.wework.Tools.Data.Cloud.ICloudEntity;
import com.xmx.wework.Tools.Data.Sync.SyncEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/6/6.
 */
public class CalendarEntity implements SyncEntity {

    public long mId = -1;
    public int mYear;
    public int mMonth;
    public int mDay;
    public int mStatus;
    public Date mTime;
    public String mCloudId;

    @Override
    public String getCloudId() {
        return mCloudId;
    }

    @Override
    public void setCloudId(String id) {
        mCloudId = id;
    }

    @Override
    public AVObject getContent(String tableName) {
        AVObject object = new AVObject(tableName);
        if (mCloudId != null) {
            object.setObjectId(mCloudId);
        }
        object.put("Year", mYear);
        object.put("Month", mMonth);
        object.put("Day", mDay);
        object.put("Status", mStatus);
        object.put("Time", mTime);
        return object;
    }

    @Override
    public ICloudEntity convertToEntity(AVObject object) {
        CalendarEntity entity = new CalendarEntity();
        entity.mCloudId = object.getObjectId();
        entity.mYear = object.getInt("Year");
        entity.mMonth = object.getInt("Month");
        entity.mDay = object.getInt("Day");
        entity.mStatus = object.getInt("Status");
        entity.mTime = object.getDate("Time");
        return entity;
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "CLOUD_ID text not null, " +
                "Year integer not null default(2016), " +
                "Month integer not null default(1), " +
                "Day integer not null default(1), " +
                "Status integer not null default(0), " +
                "Time integer not null default(0)";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("CLOUD_ID", mCloudId);
        content.put("Year", mYear);
        content.put("Month", mMonth);
        content.put("Day", mDay);
        content.put("Status", mStatus);
        content.put("Time", mTime.getTime());
        return content;
    }

    @Override
    public CalendarEntity convertToEntity(Cursor c) {
        CalendarEntity entity = new CalendarEntity();
        entity.mId = c.getLong(0);
        entity.mCloudId = c.getString(1);
        entity.mYear = c.getInt(2);
        entity.mMonth = c.getInt(3);
        entity.mDay = c.getInt(4);
        entity.mStatus = c.getInt(5);
        entity.mTime = new Date(c.getLong(6));

        return entity;
    }
}
