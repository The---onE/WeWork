package com.xmx.wework.Calendar;

import com.xmx.wework.Tools.Data.Sync.BaseSyncEntityManager;

/**
 * Created by The_onE on 2016/6/6.
 */
public class CalendarManager extends BaseSyncEntityManager<CalendarEntity> {
    private static CalendarManager instance;

    public synchronized static CalendarManager getInstance() {
        if (null == instance) {
            instance = new CalendarManager();
        }
        return instance;
    }

    private CalendarManager() {
        setTableName("Calendar");
        setEntityTemplate(new CalendarEntity());
        setUserField("User");
    }
}
