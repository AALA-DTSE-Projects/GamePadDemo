package com.huawei.gamepaddemo.slice;

import com.huawei.gamepaddemo.ResourceTable;
import com.huawei.gamepaddemo.controller.*;
import com.huawei.gamepaddemo.model.TerminateEvent;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.window.dialog.ToastDialog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.ElementName;
import ohos.bundle.IBundleManager;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.distributedschedule.interwork.IDeviceStateCallback;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.vibrator.agent.VibratorAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleAbilitySlice extends AbilitySlice {
    private static final String TAG = HandleAbilitySlice.class.getSimpleName();
    private static final int EVENT_STATE_CHANGE = 10001;
    private HandleRemoteProxy remoteProxy;
    private String deviceId;
    private final String SHOOT_ID = "Button1";
    private final String STEAL_ID = "Button2";
    private final String PASS_ID = "Button3";
    private final String PAUSE_ID = "Button4";
    private VibratorAgent vibratorAgent;

    private final EventHandler handler = new EventHandler(EventRunner.current()) {
        @Override
        protected void processEvent(InnerEvent event) {
            if (event.eventId == EVENT_STATE_CHANGE) {
                getTabletDevice();
            }
        }
    };

    private final IDeviceStateCallback callback = new IDeviceStateCallback() {
        @Override
        public void onDeviceOffline(String deviceId, int deviceType) {
            if (HandleAbilitySlice.this.deviceId.equals(deviceId)) {
                showToast("Device offline");
                disconnectAbility(connection);
            }
        }

        @Override
        public void onDeviceOnline(String deviceId, int deviceType) {
            handler.sendEvent(EVENT_STATE_CHANGE);
        }
    };

    private final IAbilityConnection connection = new IAbilityConnection() {
        @Override
        public void onAbilityConnectDone(ElementName elementName, IRemoteObject remote, int resultCode) {
            String localDeviceId = KvManagerFactory.getInstance()
                    .createKvManager(new KvManagerConfig(HandleAbilitySlice.this))
                    .getLocalDeviceInfo()
                    .getId();
            remoteProxy = new HandleRemoteProxy(remote, localDeviceId);
            LogUtil.info(TAG, "ability connect done!");
            remoteProxy.start();
            setupRemoteButton();
        }

        @Override
        public void onAbilityDisconnectDone(ElementName elementName, int i) {
            LogUtil.info(TAG, "ability disconnect done!");
            disconnectAbility(connection);
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_handle);
        initData(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onBackPressed();
    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
        remoteProxy.finish();
        disconnectAbility(connection);
        DeviceManager.unregisterDeviceStateCallback(callback);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTerminateEvent(TerminateEvent event) {
        getUITaskDispatcher().asyncDispatch(this::terminate);
    }

    private void initData(Intent intent) {
        Object obj = intent.getParams().getParam(Const.DEVICE_ID_KEY);
        if (obj instanceof String) {
            deviceId = (String) obj;
            connectToRemoteService();
        }
        vibratorAgent = new VibratorAgent();
        DeviceManager.registerDeviceStateCallback(callback);
        EventBus.getDefault().register(this);
        ScreenUtils.setWindows();
    }

    private void connectToRemoteService() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId(deviceId)
                .withBundleName(Const.BUNDLE_NAME)
                .withAbilityName(Const.ABILITY_NAME)
                .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                .build();
        intent.setOperation(operation);
        try {
            List<AbilityInfo> abilityInfoList = getBundleManager().queryAbilityByIntent(
                    intent,
                    IBundleManager.GET_BUNDLE_DEFAULT,
                    0);
            if (abilityInfoList != null && !abilityInfoList.isEmpty()) {
                connectAbility(intent, connection);
                LogUtil.info(TAG, "connect service on tablet with id " + deviceId);
            } else {
                showToast("Cannot connect service on tablet");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setupRemoteButton() {
        Image pauseButton = (Image) findComponentById(ResourceTable.Id_pause_button);
        Image shootButton = (Image) findComponentById(ResourceTable.Id_shoot_button);
        Image stealButton = (Image) findComponentById(ResourceTable.Id_steal_button);
        Image passButton = (Image) findComponentById(ResourceTable.Id_pass_button);
        Image directionCircle = (Image) findComponentById(ResourceTable.Id_direction_circle);
        Image directionButton = (Image) findComponentById(ResourceTable.Id_direction_button);
        List<Component> buttons = new ArrayList<>();
        buttons.add(pauseButton);
        buttons.add(shootButton);
        buttons.add(stealButton);
        buttons.add(passButton);
        Map<String, Image> buttonMap = new HashMap<>();
        buttonMap.put(PAUSE_ID, pauseButton);
        buttonMap.put(SHOOT_ID, shootButton);
        buttonMap.put(STEAL_ID, stealButton);
        buttonMap.put(PASS_ID, passButton);
        Map<String, int[]> buttonResources = new HashMap<>();
        buttonResources.put(PAUSE_ID, new int[]{ResourceTable.Media_play, ResourceTable.Media_pause});
        buttonResources.put(SHOOT_ID, new int[]{ResourceTable.Media_shoot_active, ResourceTable.Media_shoot_inactive});
        buttonResources.put(STEAL_ID, new int[]{ResourceTable.Media_steal_active, ResourceTable.Media_steal_inactive});
        buttonResources.put(PASS_ID, new int[]{ResourceTable.Media_pass_active, ResourceTable.Media_pass_inactive});
        Map<String, Boolean> buttonState = new HashMap<>();
        buttonState.put(PAUSE_ID, false);
        buttonState.put(SHOOT_ID, false);
        buttonState.put(STEAL_ID, false);
        buttonState.put(PASS_ID, false);

        AngleCalculator angleCalculator = new AngleCalculator(
                directionCircle,
                directionButton,
                findComponentById(ResourceTable.Id_layout),
                ScreenUtils.getScreenHeight(this),
                buttons,
                angle -> remoteProxy.move(angle)
        );

        directionButton.setTouchEventListener(angleCalculator.getOnTouchEvent());

        pauseButton.setClickedListener(component -> {
            remoteProxy.click(PAUSE_ID);
            vibrator(Const.VIBRATION);
            updateButtonState(PAUSE_ID, buttonMap, buttonResources, buttonState);
        });

        shootButton.setClickedListener(component -> {
            remoteProxy.click(SHOOT_ID);
            vibrator(Const.VIBRATION);
            updateButtonState(SHOOT_ID, buttonMap, buttonResources, buttonState);
        });

        stealButton.setClickedListener(component -> {
            remoteProxy.click(STEAL_ID);
            vibrator(Const.VIBRATION);
            updateButtonState(STEAL_ID, buttonMap, buttonResources, buttonState);
        });

        passButton.setClickedListener(component -> {
            remoteProxy.click(PASS_ID);
            vibrator(Const.VIBRATION);
            updateButtonState(PASS_ID, buttonMap, buttonResources, buttonState);
        });
    }

    private void getTabletDevice() {
        List<DeviceInfo> devices = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        if (devices.isEmpty()) {
            showToast("No device found");
        } else {
            devices.forEach(deviceInfo -> {
                DeviceInfo.DeviceType deviceType = deviceInfo.getDeviceType();
                String deviceId = deviceInfo.getDeviceId();
                LogUtil.info(TAG, "Found device " + deviceInfo.getDeviceType());
                if (deviceType == DeviceInfo.DeviceType.SMART_PAD && !deviceId.equals(this.deviceId)) {
                    this.deviceId = deviceId;
                    connectToRemoteService();
                }
            });
        }
    }

    private void showToast(String text) {
        LogUtil.info(TAG, text);
        new ToastDialog(this)
                .setText(text)
                .setAutoClosable(false)
                .show();
    }

    private void vibrator(int duration) {
        List<Integer> vibratorList = vibratorAgent.getVibratorIdList();
        if (vibratorList.isEmpty()) {
            return;
        }
        int vibratorId = vibratorList.get(0);
        vibratorAgent.startOnce(vibratorId, duration);
    }

    private void updateButtonState(
            String buttonId,
            Map<String, Image> buttonMap,
            Map<String, int[]> buttonResources,
            Map<String, Boolean> buttonState
    ) {
        buttonState.keySet().forEach(id -> buttonState.put(id, id.equals(buttonId)));
        buttonMap.forEach((id, button) -> {
            boolean isActive = buttonState.get(id);
            int[] resources = buttonResources.get(id);
            button.setPixelMap(isActive ? resources[0] : resources[1]);
        });
    }
}
