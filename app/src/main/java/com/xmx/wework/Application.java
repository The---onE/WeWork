package com.xmx.wework;

import com.avos.avoscloud.AVOSCloud;
import com.xmx.wework.Tools.Data.DataManager;
import com.xmx.wework.User.UserManager;

/**
 * Created by The_onE on 2016/1/3.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, Constants.APP_ID, Constants.APP_KEY);
        UserManager.getInstance().setContext(this);

        DataManager.getInstance().setContext(this);
    }
}
