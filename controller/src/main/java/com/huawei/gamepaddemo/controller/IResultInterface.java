package com.huawei.gamepaddemo.controller;

import ohos.rpc.IRemoteBroker;

public interface IResultInterface extends IRemoteBroker {
    void connect(String deviceId);
    void disconnect(String deviceId);
}
