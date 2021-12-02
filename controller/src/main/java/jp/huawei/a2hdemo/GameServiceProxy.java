package jp.huawei.a2hdemo;

import com.huawei.gamepaddemo.controller.LogUtil;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class GameServiceProxy implements IGameInterface {

    private static final String TAG = GameServiceProxy.class.getName();
    private final IRemoteObject remoteObject;

    public GameServiceProxy(IRemoteObject remoteObject) {
        this.remoteObject = remoteObject;
    }

    @Override
    public void start(String deviceId) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(GameServiceStub.DESCRIPTOR);
        data.writeString(deviceId);

        try {
            remoteObject.sendRequest(GameServiceStub.START_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "Start action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public void move(String deviceId, int angle) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(GameServiceStub.DESCRIPTOR);
        data.writeString(deviceId);
        data.writeInt(angle);

        try {
            remoteObject.sendRequest(GameServiceStub.MOVE_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "move action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public void buttonPress(String deviceId, String buttonId) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(GameServiceStub.DESCRIPTOR);
        data.writeString(deviceId);
        data.writeString(buttonId);

        try {
            remoteObject.sendRequest(GameServiceStub.PRESS_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "press button action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public void buttonRelease(String deviceId, String buttonId) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(GameServiceStub.DESCRIPTOR);
        data.writeString(deviceId);
        data.writeString(buttonId);

        try {
            remoteObject.sendRequest(GameServiceStub.RELEASE_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "release button action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public void finish(String deviceId) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(GameServiceStub.DESCRIPTOR);
        data.writeString(deviceId);

        try {
            remoteObject.sendRequest(GameServiceStub.FINISH_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "finish action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public IRemoteObject asObject() {
        return remoteObject;
    }
}
