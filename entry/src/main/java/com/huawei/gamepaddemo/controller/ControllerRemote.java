package com.huawei.gamepaddemo.controller;

import com.huawei.gamepaddemo.model.TerminateEvent;
import ohos.rpc.*;
import org.greenrobot.eventbus.EventBus;

public class ControllerRemote extends RemoteObject implements IRemoteBroker {
    static final int TERMINATE_COMMAND = RemoteObject.MIN_TRANSACTION_ID;

    public ControllerRemote(String descriptor) {
        super(descriptor);
    }

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    @Override
    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
        if (code == TERMINATE_COMMAND) {
            EventBus.getDefault().post(new TerminateEvent());
        }
        return false;
    }
}
