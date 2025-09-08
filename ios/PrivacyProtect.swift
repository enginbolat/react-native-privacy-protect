import Foundation
import UIKit

@objc(PrivacyProtect)
class PrivacyProtect: NSObject {

  private static var blurView: UIVisualEffectView?

  override init() {
    super.init()

    NotificationCenter.default.addObserver(
      self,
      selector: #selector(appWillResignActive),
      name: UIApplication.willResignActiveNotification,
      object: nil
    )
    NotificationCenter.default.addObserver(
      self,
      selector: #selector(appDidBecomeActive),
      name: UIApplication.didBecomeActiveNotification,
      object: nil
    )
  }

  @objc func appWillResignActive() {
    guard let window = UIApplication.shared.connectedScenes
      .compactMap({ $0 as? UIWindowScene })
      .flatMap({ $0.windows })
      .first(where: { $0.isKeyWindow }) else { return }

    if PrivacyProtect.blurView == nil {
      let blur = UIBlurEffect(style: .dark)
      let view = UIVisualEffectView(effect: blur)
      view.frame = window.bounds
      view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
      PrivacyProtect.blurView = view
    }

    if let blurView = PrivacyProtect.blurView, blurView.superview == nil {
      window.addSubview(blurView)
    }
  }

  @objc func appDidBecomeActive() {
    PrivacyProtect.blurView?.removeFromSuperview()
  }
}
