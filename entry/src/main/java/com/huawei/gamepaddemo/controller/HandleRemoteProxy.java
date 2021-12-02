package com.huawei.gamepaddemo.controller;

import ohos.rpc.*;

public class HandleRemoteProxy implements IRemoteBroker {
    private static final int START_COMMAND = 0;
    private static final int MOVE_COMMAND = 1;
    private static final int PRESS_COMMAND = 2;
    private static final int RELEASE_COMMAND = 3;
    private static final int FINISH_COMMAND = 4;
    private final String TAG = HandleRemoteProxy.class.getSimpleName();
    private final IRemoteObject remote;
    private final String deviceId;

    public HandleRemoteProxy(IRemoteObject remote, String deviceId) {
        this.remote = remote;
        this.deviceId = deviceId;
    }

    @Override
    public IRemoteObject asObject() {
        return remote;
    }

    public void start() {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        data.writeString(deviceId);
        try {
            remote.sendRequest(START_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "remote action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    public void move(int angle) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        data.writeString(deviceId);
        data.writeInt(angle);
        try {
            remote.sendRequest(MOVE_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "move with angle action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    public void press(String buttonId) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        data.writeString(deviceId);
        data.writeString(buttonId);
        try {
            remote.sendRequest(PRESS_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "press action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    public void release(String buttonId) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        data.writeString(deviceId);
        data.writeString(buttonId);
        try {
            remote.sendRequest(RELEASE_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "release action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    public void finish() {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        data.writeString(deviceId);
        try {
            remote.sendRequest(FINISH_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "remote action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }
}
