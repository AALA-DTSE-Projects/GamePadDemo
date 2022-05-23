package com.huawei.gamepaddemo.controller;

import jp.huawei.a2hdemo.GameServiceStub;
import jp.huawei.a2hdemo.IGameInterface;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.bundle.ElementName;
import ohos.rpc.*;

public class GameRemote extends RemoteObject implements IRemoteBroker {
    private final String TAG = GameRemote.class.getName();
    static final int START_COMMAND = 0;
    static final int MOVE_COMMAND = 1;
    static final int CLICK_COMMAND = 2;
    static final int FINISH_COMMAND = 3;
    private final Ability ability;
    private boolean isConnected;
    private IGameInterface remoteService;
    private String firstDeviceId;

    private final IAbilityConnection connection = new IAbilityConnection() {
        @Override
        public void onAbilityConnectDone(ElementName elementName, IRemoteObject remote, int resultCode) {
            remoteService = GameServiceStub.asInterface(remote);
            LogUtil.info(TAG, "Android service connect done!");
            if (firstDeviceId != null) {
                remoteService.start(firstDeviceId);
            }
        }

        @Override
        public void onAbilityDisconnectDone(ElementName elementName, int i) {
            LogUtil.info(TAG, "Android service disconnect done!");
            isConnected = false;
            ability.disconnectAbility(connection);
        }
    };

    public GameRemote(Ability ability) {
        super("Game remote");
        this.ability = ability;
    }

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    @Override
    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
        switch (code) {
            case START_COMMAND:
                String deviceId = data.readString();
                if (!isConnected) {
                    startAndroidApp();
                    connectToAndroidService();
                    this.firstDeviceId = deviceId;
                } else {
                    start(deviceId);
                }
                return true;
            case MOVE_COMMAND:
                move(data.readString(), data.readInt());
                return true;
            case CLICK_COMMAND:
                click(data.readString(), data.readString());
                return true;
            case FINISH_COMMAND:
                finish(data.readString());
                return true;
        }
        return false;
    }

    private void startAndroidApp() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(Const.ANDROID_PACKAGE_NAME)
                .withAbilityName(Const.ANDROID_ACTIVITY_NAME)
                .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT)
                .build();
        intent.setOperation(operation);
        ability.startAbility(intent);
    }

    private void connectToAndroidService() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(Const.ANDROID_PACKAGE_NAME)
                .withAbilityName(Const.ANDROID_SERVICE_NAME)
                .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT)
                .build();
        intent.setOperation(operation);
        isConnected = ability.connectAbility(intent, connection);
    }

    private void start(String deviceId) {
        if (remoteService != null) {
            remoteService.start(deviceId);
        }
    }

    private void move(String deviceId, int angle) {
        if (remoteService != null) {
            remoteService.move(deviceId, angle);
        }
    }

    private void click(String deviceId, String buttonId) {
        if (remoteService != null) {
            remoteService.buttonClick(deviceId, buttonId);
        }
    }

    private void finish(String deviceId) {
        if (remoteService != null) {
            remoteService.finish(deviceId);
        }
    }
}
