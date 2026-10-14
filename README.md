# PoilabsVdNavigation React Native Integration

| | iOS | Android |
|---|---|---|
| PoilabsVdNavigation | 7.2.2 | 7.1.0 |
| Minimum OS | iOS 12.0 | Android 5.0 (API 21) |
| Installation | Swift Package Manager | Gradle (JitPack) |

Supported languages: `en`, `tr`, `hr`, `de`, `ar`, `pl`.

## iOS

### INSTALLATION

PoilabsVdNavigation is distributed with Swift Package Manager. CocoaPods is no longer supported for the SDK (your React Native Podfile stays as it is for React Native's own pods).

#### Swift Package Manager

This sample project uses Swift Package Manager.

1. Open `ios/<YOUR_PROJECT_NAME>.xcworkspace` in Xcode.
2. Select **File > Add Package Dependencies...**
3. Enter the repository URL: `https://github.com/poiteam/ios-vd-navigation-pod.git`
4. Choose **Exact Version** `7.2.2` and add the **PoilabsVdNavigation** product to your app target.

PoilabsPositioning, PoilabsSdkAnalytics and PoilabsCore are resolved automatically. Do not add `pod 'PoilabsVdNavigation'` to your Podfile; installing the SDK with both SPM and CocoaPods causes duplicate symbols. If your Podfile already has it, remove it (together with any `PoilabsCore`, `PoilabsPositioning` or `PoilabsSdkAnalytics` lines) and run `pod install`.

SPM installation is supported from 7.2.1.

> React Native 0.73 build scripts fail when the project path contains spaces. Keep the project in a path without spaces.

### PRE-REQUIREMENTS

To Integrate this framework you should add some features to your project info.plist file.

+Privacy - Location Usage Description

+Privacy - Location When In Use Usage Description

+Privacy - Bluetooth Peripheral Usage Description

+Privacy - Bluetooth Always Usage Description

### USAGE

Create a Swift file named **PoilabsVdNavigationManager** with content below. It starts the SDK and presents its screen.

Replace `APPLICATION_ID` and `APPLICATION_SECRET_KEY` with the values provided by Poilabs. `UNIQUE_ID` must be unique for every app user.

The initializer returns the SDK's view controller in its completion handler. Keep a reference to the `PoilabsVdNavigationUI` instance while the SDK screen is open; the shared manager below does that.

```Swift
import UIKit
import PoilabsVdNavigationUI

@objc class PoilabsVdNavigationManager: NSObject {
    @objc static let shared = PoilabsVdNavigationManager()

    private var poilabsVdNavigation: PoilabsVdNavigationUI?

    @objc func showPoilabsVdNavigation() {
        poilabsVdNavigation = PoilabsVdNavigationUI(withApplicationID: "APPLICATION_ID",
                                                    withApplicationSecret: "APPLICATION_SECRET_KEY",
                                                    withUniqueIdentifier: "UNIQUE_ID") { controller in
            DispatchQueue.main.async {
                let keyWindow = UIApplication.shared.windows.first(where: { $0.isKeyWindow }) ?? UIApplication.shared.windows.first
                var topController = keyWindow?.rootViewController
                while let presented = topController?.presentedViewController {
                    topController = presented
                }
                controller.modalPresentationStyle = .fullScreen
                topController?.present(controller, animated: true)
            }
        }
    }
}
```

You should create a header file called **PoilabsNavigationBridge.h** and a Objective-C file  called **PoilabsNavigationBridge.m** with content below. 

Do not forget to change **YOUR_PROJECT_NAME** with your project name.
 
```c
#ifndef PoilabsNavigationBridge_h
#define PoilabsNavigationBridge_h

#import <React/RCTBridgeModule.h>

@interface PoilabsNavigationBridge : NSObject <RCTBridgeModule>

-(void) showPoilabsVdNavigation;

@end
#endif /* PoilabsNavigationBridge_h */
```

```c
#import <Foundation/Foundation.h>
#import "PoilabsNavigationBridge.h"
#import "YOUR_PROJECT_NAME-Swift.h"

@implementation PoilabsNavigationBridge: NSObject


RCT_EXPORT_MODULE(PoilabsNavigationBridge);

RCT_EXPORT_METHOD(showPoilabsVdNavigation) {
  dispatch_async(dispatch_get_main_queue(), ^{
    [PoilabsVdNavigationManager.shared showPoilabsVdNavigation];
  });
}

@end
```

## Android

### INSTALLATION

You can download our SDK via Gradle with following below steps


*  Add the JitPack repository to your project level build.gradle file.
   **JITPACK_TOKEN** is a token that PoiLabs will provide for you; it allows you to download our SDK.
   Since 7.0.7 the SDK no longer uses Mapbox, so the Mapbox repository and token are not needed.

~~~groovy  
allprojects {  
      repositories {  
          maven {  
               url "https://jitpack.io" 
               credentials { username = 'JITPACK_TOKEN' }  
          }  
      }  
}
~~~  

* Add PoiLabs Vd Navigation SDK dependency to your app level build.gradle file

~~~groovy  
  
dependencies {  
     implementation 'com.github.poiteam:Android-VD-Navigation-SDK:7.1.0'
 }  
~~~ 

# Prerequirements

In order to our SDK can work properly we need location permission and bluetooth usage for scanning
for beacons by using our sdk the below permissions will add to your manifest file.

 ~~~
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
    <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
~~~

### USAGE

Create a java file named **PoilabsPackage** with content below.

```Java
import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Collections;
import java.util.List;

public class PoilabsPackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        return Collections.singletonList(new PoilabsNavigationBridge(reactContext));
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
```

Create a Kotlin file call PoilabsNavigationBridge
```Kotlin
import android.content.Intent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.getpoi.android_vd_nav_ui.view.PoiVdNavigationActivity
import com.poilabs.vd.nav.non.ui.jsonclient.VDResponseListener
import com.poilabs.vd.nav.non.ui.models.PoiManager

class PoilabsNavigationBridge internal constructor(context: ReactApplicationContext?) :
    ReactContextBaseJavaModule(context) {
    override fun getName(): String {
        return "PoilabsNavigationBridge"
    }
    @ReactMethod
    fun showPoilabsVdNavigation() {
        currentActivity?.let { activity ->
            PoiManager.init(
                context = activity.baseContext,
                appId = "APPLICATION_ID",
                secret = "APPLICATION_SECRET_KEY",
                language = "en", // en, tr, hr, de, ar, pl
                title = "PLACE_TITLE",
                vdResponseListener = object : VDResponseListener {
                    override fun onSuccess() {
                        PoiManager.setUniqueId("UNIQUE_ID")
                        Intent(activity.applicationContext, PoiVdNavigationActivity::class.java).also {
                            activity.startActivity(it)
                        }
                    }
                    override fun onFail(throwable: Throwable?) {

                    }
                })
        }
    }
}
```

Open up your MainApplication.java file, which can be found in the following path: android/app/src/main/java/com/your-app-name/MainApplication.kt

Locate ReactNativeHost’s getPackages() method and add your package to the packages list getPackages() returns:

```Java
    override fun getPackages(): List<ReactPackage> =
        PackageList(this).packages.apply {
          add(PoilabsPackage())
        }
```


## React Native

You should import **NativeModules**

```js
import {
  NativeModules,
} from 'react-native';
```
You can start PoilabsVdNavigation with calling bridge method

```js
NativeModules.PoilabsNavigationBridge.showPoilabsVdNavigation();
```




