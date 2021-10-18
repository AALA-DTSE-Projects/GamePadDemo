package com.huawei.gamepaddemo.controller;

import ohos.rpc.*;

public class HandleRemoteProxy implements IRemoteBroker {
    private static final int REMOTE_COMMAND = 0;
    private static final int SHOOT_COMMAND = 1;
    private static final int MOVE_COMMAND = 2;
    private static final int PAUSE_COMMAND = 3;
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

    public void remoteControl(String action) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        data.writeString(deviceId);
        data.writeString(action);
        try {
            remote.sendRequest(REMOTE_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "remote action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    public void shoot(float force) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        data.writeString(deviceId);
        data.writeFloat(force);
        try {
            remote.sendRequest(SHOOT_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "shoot with force action error " + e.getMessage());
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

    public void pause() {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        data.writeString(deviceId);
        try {
            remote.sendRequest(PAUSE_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "pause action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }
}
