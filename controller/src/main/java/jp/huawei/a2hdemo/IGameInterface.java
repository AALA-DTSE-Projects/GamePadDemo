package jp.huawei.a2hdemo;

import ohos.rpc.IRemoteBroker;

public interface IGameInterface extends IRemoteBroker {
    void action(String deviceId, String action);
    void shoot(String deviceId, float force);
    void move(String deviceId, int angle);
    void pause(String deviceId);
}
