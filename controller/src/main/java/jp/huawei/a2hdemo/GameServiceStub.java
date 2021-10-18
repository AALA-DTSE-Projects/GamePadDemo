package jp.huawei.a2hdemo;

import ohos.rpc.*;

public abstract class GameServiceStub extends RemoteObject implements IGameInterface {

    static final String DESCRIPTOR = "jp.huawei.a2hdemo.IGameInterface";
    static final int REMOTE_COMMAND = IRemoteObject.MIN_TRANSACTION_ID;
    static final int SHOOT_COMMAND = IRemoteObject.MIN_TRANSACTION_ID + 1;
    static final int MOVE_COMMAND = IRemoteObject.MIN_TRANSACTION_ID + 2;
    static final int PAUSE_COMMAND = IRemoteObject.MIN_TRANSACTION_ID + 3;

    public GameServiceStub(String descriptor) {
        super(descriptor);
    }

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    public static IGameInterface asInterface(IRemoteObject remoteObject) {
        if (remoteObject == null) {
            return null;
        }
        IRemoteBroker broker = remoteObject.queryLocalInterface(DESCRIPTOR);
        if (broker instanceof IGameInterface) {
            return (IGameInterface) broker;
        } else {
            return new GameServiceProxy(remoteObject);
        }
    }

    @Override
    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) throws RemoteException {
        String token = data.readInterfaceToken();
        if (!DESCRIPTOR.equals(token)) {
            return false;
        }
        switch (code) {
            case REMOTE_COMMAND:
                action(data.readString(), data.readString());
                return true;
            case SHOOT_COMMAND:
                shoot(data.readString(), data.readFloat());
                return true;
            case MOVE_COMMAND:
                move(data.readString(), data.readInt());
                return true;
            case PAUSE_COMMAND:
                pause(data.readString());
                return true;
            default:
                return super.onRemoteRequest(code, data, reply, option);
        }
    }
}
