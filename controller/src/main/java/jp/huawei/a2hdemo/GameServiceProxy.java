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
    public void action(String deviceId, String action) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(GameServiceStub.DESCRIPTOR);
        data.writeString(deviceId);
        data.writeString(action);

        try {
            remoteObject.sendRequest(GameServiceStub.REMOTE_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "remote action error " + e.getMessage());
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public void shoot(String deviceId, float force) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(GameServiceStub.DESCRIPTOR);
        data.writeString(deviceId);
        data.writeFloat(force);

        try {
            remoteObject.sendRequest(GameServiceStub.SHOOT_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "shoot action with force error " + e.getMessage());
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
    public void pause(String deviceId) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(GameServiceStub.DESCRIPTOR);
        data.writeString(deviceId);

        try {
            remoteObject.sendRequest(GameServiceStub.PAUSE_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "pause action error " + e.getMessage());
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
