package jp.huawei.a2hdemo;

import ohos.rpc.*;

public abstract class GameServiceStub extends RemoteObject implements IGameInterface {

    static final String DESCRIPTOR = "jp.huawei.a2hdemo.IGameInterface";
    static final int START_COMMAND = IRemoteObject.MIN_TRANSACTION_ID;
    static final int MOVE_COMMAND = IRemoteObject.MIN_TRANSACTION_ID + 1;
    static final int CLICK_COMMAND = IRemoteObject.MIN_TRANSACTION_ID + 2;
    static final int FINISH_COMMAND = IRemoteObject.MIN_TRANSACTION_ID + 3;

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
            case START_COMMAND:
                start(data.readString());
                return true;
            case MOVE_COMMAND:
                move(data.readString(), data.readInt());
                return true;
            case CLICK_COMMAND:
                buttonClick(data.readString(), data.readString());
                return true;
            case FINISH_COMMAND:
                finish(data.readString());
                return true;
            default:
                return super.onRemoteRequest(code, data, reply, option);
        }
    }
}
