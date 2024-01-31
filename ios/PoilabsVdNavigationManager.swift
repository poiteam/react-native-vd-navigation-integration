//
//  PoilabsVdNavigationManager.swift
//  reactNativeVdNavigationIntegration
//
//  Created by Emre Kuru on 31.01.2024.
//

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
