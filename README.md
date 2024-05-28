# PoilabsVdNavigation React Native Integration

## iOS

### INSTALLATION

To integrate PoilabsVdNavigation into your Xcode project using CocoaPods, specify it in your Podfile. 

Also you should enable **use_frameworks!**

use\_frameworks option should be before use\_react\_native!() function call. It will avoid getting multiple command produce error

```curl

use_frameworks!
.....
  use_react_native!(
    .....
  )
.....
pod 'PoilabsVdNavigation'

```

### PRE-REQUIREMENTS

To Integrate this framework you should add some features to your project info.plist file.

+Privacy - Location Usage Description

+Privacy - Location When In Use Usage Description

+Privacy - Bluetooth Peripheral Usage Description

+Privacy - Bluetooth Always Usage Description

### USAGE

Create a Swift file named **PoilabsVdNavigationManager** with content below. This will be your Map View. 

Do not forget change application id and application secret key with those which are given by Poilabs.

```Swift
import UIKit
import PoilabsVdNavigationUI

@objc  class PoilabsVdNavigationManager: NSObject  {
    @objc func showPoilabsVdNavigation() {
        let appId = "applicationId"
        let secret = "applicationSecret"
        let uniqueIdentifier = "UNIQUE_ID"
    
        let _ = PoilabsVdNavigationUI(withApplicationID: appId, withApplicationSecret: secret, withUniqueIdentifier: uniqueIdentifier) { controller in
            DispatchQueue.main.async {
            let keyWindow = UIApplication.shared.windows.first(where: { $0.isKeyWindow }) ?? UIApplication.shared.windows.first
            let topController = keyWindow?.rootViewController
          topController?.show(controller, sender: self)
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
    PoilabsVdNavigationManager* vdManager = [[PoilabsVdNavigationManager alloc] init];
    [vdManager showPoilabsVdNavigation];
  });
}

@end
```

## Android

### INSTALLATION

You can download our SDK via Gradle with following below steps


*  Add jitpack and mapbox dependency to your project level build.gradle file with their tokens.
   **JITPACK_TOKEN** is a token that PoiLabs will provide for you it will allow you to download our sdk.

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
     implementation 'com.github.poiteam:Android-VD-Navigation-SDK:v6.2.7'  
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
                language = "en",
                title = "PLACE_TITLE",
                vdResponseListener = object : VDResponseListener {
                    override fun onSuccess() {
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




