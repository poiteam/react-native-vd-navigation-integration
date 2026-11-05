# PoilabsVdNavigation React Native Integration

| | iOS | Android |
|---|---|---|
| PoilabsVdNavigation | 7.3.0 | 7.3.0 |
| Minimum OS | iOS 12.0 | Android 5.0 (API 21) |
| Installation | Swift Package Manager | Gradle (JitPack) |

Supported languages: `en`, `tr`, `hr`, `de`, `ar`, `pl`.

If your application has more than one venue, you can open one of them directly; see [Opening a specific venue (placeId)](#opening-a-specific-venue-placeid).

## iOS

### INSTALLATION

PoilabsVdNavigation is distributed with Swift Package Manager. CocoaPods is no longer supported for the SDK (your React Native Podfile stays as it is for React Native's own pods).

#### Swift Package Manager

This sample project uses Swift Package Manager.

1. Open `ios/<YOUR_PROJECT_NAME>.xcworkspace` in Xcode.
2. Select **File > Add Package Dependencies...**
3. Enter the repository URL: `https://github.com/poiteam/ios-vd-navigation-pod.git`
4. Choose **Exact Version** `7.3.0` and add the **PoilabsVdNavigation** product to your app target.

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

@objc class PoilabsVdNavigationManager: NSObject, PoilabsVdNavigationDelegate {
    @objc static let shared = PoilabsVdNavigationManager()

    private var poilabsVdNavigation: PoilabsVdNavigationUI?

    /// placeId is optional: nil or empty opens the venue list.
    @objc func showPoilabsVdNavigation(placeId: String?) {
        poilabsVdNavigation = PoilabsVdNavigationUI(withApplicationID: "APPLICATION_ID",
                                                    withApplicationSecret: "APPLICATION_SECRET_KEY",
                                                    withUniqueIdentifier: "UNIQUE_ID",
                                                    // The SDK screen has no close button of its own; the SDK shows
                                                    // this one on its first screen and calls the delegate below.
                                                    additionalButton: AdditionalButton(icon: UIImage(systemName: "xmark") ?? UIImage(),
                                                                                       title: "Close"),
                                                    placeId: placeId) { controller in
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
        poilabsVdNavigation?.delegate = self
    }

    // Called when the "Close" button is tapped: dismiss the SDK screen.
    func poilabsVdNavigationDidTapOnAdditionalButton() {
        let keyWindow = UIApplication.shared.windows.first(where: { $0.isKeyWindow }) ?? UIApplication.shared.windows.first
        keyWindow?.rootViewController?.dismiss(animated: true)
        poilabsVdNavigation = nil
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

-(void) showPoilabsVdNavigation:(NSString *)placeId;

@end
#endif /* PoilabsNavigationBridge_h */
```

```c
#import <Foundation/Foundation.h>
#import "PoilabsNavigationBridge.h"
#import "YOUR_PROJECT_NAME-Swift.h"

@implementation PoilabsNavigationBridge: NSObject


RCT_EXPORT_MODULE(PoilabsNavigationBridge);

RCT_EXPORT_METHOD(showPoilabsVdNavigation:(NSString *)placeId) {
  dispatch_async(dispatch_get_main_queue(), ^{
    [PoilabsVdNavigationManager.shared showPoilabsVdNavigationWithPlaceId:placeId];
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
     implementation 'com.github.poiteam:Android-VD-Navigation-SDK:7.3.0'
 }  
~~~ 

* The SDK still depends on some old `com.android.support` libraries. Enable Jetifier in `android/gradle.properties`, otherwise the build fails with `Duplicate class android.support.v4...`. Skipping React Native's own libraries keeps Jetifier fast:

~~~properties
android.enableJetifier=true
android.jetifier.ignorelist=react-android,hermes-android
~~~

* React Native's template sets `android:usesCleartextTraffic` in `AndroidManifest.xml`. Keep your value with `tools:replace="android:usesCleartextTraffic"` on `<application>` (add `xmlns:tools="http://schemas.android.com/tools"` to `<manifest>`); otherwise the release build fails with `Manifest merger failed : Attribute application@usesCleartextTraffic`.

* React Native 0.80 and later target Android 15+ (targetSdk 35/36). Two things are needed for the SDK screen:

  1. **Back button:** Android 16 uses predictive back for apps targeting 36 and no longer calls the SDK screen's `onBackPressed()`; back then leaves an empty page. Keep the old back handling for the SDK screen in `android/app/src/main/AndroidManifest.xml`:

     ~~~xml
     <application ...>
         <activity
             android:name="com.getpoi.android_vd_nav_ui.view.PoiVdNavigationActivity"
             android:enableOnBackInvokedCallback="false"
             tools:node="merge"
             tools:targetApi="33" />
     </application>
     ~~~

  2. **Status bar:** Android 15 draws every activity edge-to-edge, and the SDK toolbar is drawn under the status bar. Give the SDK screen the system bar sizes as padding, for example in `MainApplication.onCreate()`:

     ~~~kotlin
     registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
         override fun onActivityStarted(activity: Activity) {
             if (activity !is PoiVdNavigationActivity) return
             val content = activity.findViewById<View>(android.R.id.content)
             ViewCompat.setOnApplyWindowInsetsListener(content) { view, insets ->
                 val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
                 view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
                 insets
             }
             ViewCompat.requestApplyInsets(content)
             // Dark status bar icons on the light background
             WindowCompat.getInsetsController(activity.window, activity.window.decorView).isAppearanceLightStatusBars = true
         }
         override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
         override fun onActivityResumed(activity: Activity) {}
         override fun onActivityPaused(activity: Activity) {}
         override fun onActivityStopped(activity: Activity) {}
         override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
         override fun onActivityDestroyed(activity: Activity) {}
     })
     ~~~

* The SDK screen closes with the back button. To show a close button as well, pass `navigationButton = NavigationButton("Close", R.drawable.ic_close, object : NavButtonClickCallback { override fun onClick() { activity.finishActivity(VD_REQUEST_CODE) } })` to `PoiManager.init` and start `PoiVdNavigationActivity` with `startActivityForResult(intent, VD_REQUEST_CODE)`. The SDK shows the icon in its toolbar and uses the text for TalkBack.

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
    // placeId is optional: null or empty opens the venue list.
    @ReactMethod
    fun showPoilabsVdNavigation(placeId: String?) {
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
                },
                placeId = placeId)
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
You can start PoilabsVdNavigation with calling bridge method. Pass `null` to open the venue list:

```js
NativeModules.PoilabsNavigationBridge.showPoilabsVdNavigation(null);
```

## Opening a specific venue (placeId)

Normally the SDK opens with the venue list of your application. If you want to open one venue directly, pass its `placeId` to the bridge method. The SDK then skips the venue list and opens that venue's store list as the first screen.

```js
NativeModules.PoilabsNavigationBridge.showPoilabsVdNavigation('PLACE_ID');
```

- The `placeId` must be one of the venues of your application: a venue in its venue list, or a venue listed under one of them. Ask PoiLabs for the `placeId` values of your venues.
- If you pass `null`, an empty string or a `placeId` that is not one of your venues, the SDK opens as before (the venue list). For an unknown `placeId` the SDK writes a warning to the native log.
- Always pass the argument: React Native bridge methods must be called with every parameter, so use `null` when you do not want a specific venue.
- `placeId` needs **PoilabsVdNavigation 7.3.0** on both platforms (the native code above already passes it to the SDK).
- To open another venue, close the SDK screen and call `showPoilabsVdNavigation` again with the other `placeId`.

## Using together with the PoiLabs Navigation SDK

Both SDKs use the same positioning and Bluetooth scanning. Do not keep both open at the same time: remove the Navigation map before you call `showPoilabsVdNavigation`, and show it again after the VD screen is closed.

Example: a button for each venue.

```js
import React from 'react';
import { Button, NativeModules, View } from 'react-native';

const VENUES = [
  { title: 'All venues', placeId: null },   // null → the venue list
  { title: 'Venue A', placeId: 'PLACE_ID_A' },
  { title: 'Venue B', placeId: 'PLACE_ID_B' },
];

export default function VenueButtons() {
  return (
    <View>
      {VENUES.map(venue => (
        <Button
          key={venue.title}
          title={venue.title}
          onPress={() => NativeModules.PoilabsNavigationBridge.showPoilabsVdNavigation(venue.placeId)}
        />
      ))}
    </View>
  );
}
```




