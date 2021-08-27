# GamePadDemo

 Game controller scenario's Harmony OS demo app. </br>
 User can use Harmony OS smartphone to control the game on Harmony OS tablet
 
## Structure
There are 2 modules:
- entry: handler module, need to deploy on mobile phone to control the game
- controller: controller module, used to connect with Android app to do the control

## How to deploy
- Deploy the **entry** module to the control device such as mobile phone or tablet (you can deploy up to 2 devices to try the dual controllers scenario)
- Deploy the **controller** module with the [Android demo app](https://github.com/AALA-DTSE-Projects/GamePadDemo-Android-) on the target device such as tablet or TV 
- Open the **controller** app on the target device then grant multi-device collaboration permission
- Open the **Android** app on the target device then grant multi-device collaboration permission
- Open the **entry** app on the control device then grant multi-device collaboration permission 
- Connect all devices in the same wifi network
- Login with the same Huawei ID on all devices
- Set up bluetooth connection between all devices
- Open the **entry** app on the control device then click the device name to start the app on target device
- You can control the human position on the target device by click the arrow button on the control device

## Demo result
![](screenshot/gamePadDemo.gif)
