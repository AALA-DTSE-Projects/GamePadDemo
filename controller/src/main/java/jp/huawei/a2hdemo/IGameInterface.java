package jp.huawei.a2hdemo;

import ohos.rpc.IRemoteBroker;

public interface IGameInterface extends IRemoteBroker {
    void start(String deviceId);
    void move(String deviceId, int angle);
    void buttonPress(String deviceId, String buttonId);
    void buttonRelease(String deviceId, String buttonId);
    void finish(String deviceId);
}
