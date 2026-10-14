//
//  PoilabsVdNavigationManager.swift
//  reactNativeVdNavigationIntegration
//
//  Created by Emre Kuru on 31.01.2024.
//

import UIKit
import PoilabsVdNavigationUI

@objc class PoilabsVdNavigationManager: NSObject {
    @objc static let shared = PoilabsVdNavigationManager()

    // Keep a reference while the SDK screen is open.
    private var poilabsVdNavigation: PoilabsVdNavigationUI?

    @objc func showPoilabsVdNavigation() {
        poilabsVdNavigation = PoilabsVdNavigationUI(withApplicationID: "applicationId",
                                                    withApplicationSecret: "applicationSecret",
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
