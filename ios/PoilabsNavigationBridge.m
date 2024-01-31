//
//  PoilabsNavigationBridge.m
//  reactNativeVdNavigationIntegration
//
//  Created by Emre Kuru on 31.01.2024.
//

#import <Foundation/Foundation.h>
#import "PoilabsNavigationBridge.h"
#import "reactNativeVdNavigationIntegration-Swift.h"

@implementation PoilabsNavigationBridge: NSObject


RCT_EXPORT_MODULE(PoilabsNavigationBridge);

RCT_EXPORT_METHOD(showPoilabsVdNavigation) {
  dispatch_async(dispatch_get_main_queue(), ^{
    PoilabsVdNavigationManager* vdManager = [[PoilabsVdNavigationManager alloc] init];
    [vdManager showPoilabsVdNavigation];
  });
}

@end
