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

### USAGE

## React Native

You should import **NativeModules**

### iOS

You can start PoilabsVdNavigation with calling bridge method

```js
NativeModules.PoilabsNavigationBridge.showPoilabsVdNavigation();
```

### Android



