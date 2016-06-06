package com.xmx.wework.Tools.Data.Callback;

import com.avos.avoscloud.AVException;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class UpdateCallback {

    public abstract void success();

    public abstract void notInit();

    public abstract void syncError(AVException e);

    public abstract void notLoggedIn();

    public abstract void errorNetwork();

    public abstract void errorUsername();

    public abstract void errorChecksum();
}
