import React
import UIKit

@objc(PrivacyProtect)
class PrivacyProtect: NSObject {
  private static var blurView: UIVisualEffectView?
  private static var overlayView: UIView?
  private static var imageView: UIImageView?
  private static var config: [String: Any] = [
    "blurStyle": "dark",
    "overlayColor": "#00000080",
    "animated": true,
    "animationDuration": 300,
    "backgroundImage": NSNull()
  ]
  
  @objc func configure(_ options: NSDictionary) {
    for (key, value) in options {
      PrivacyProtect.config[key as! String] = value
    }

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
  
  private func parseBlurStyle(_ name: String) -> UIBlurEffect.Style {
    switch name {
    case "light": return .light
    case "extraLight": return .extraLight
    default: return .dark
    }
  }
  
  private func resolveColor(_ value: Any?) -> UIColor? {
    if let number = value as? NSNumber {
      return RCTConvert.uiColor(number) // processColor’dan int
    }
    if let str = value as? String {
      return UIColor(hex: str)
    }
    return nil
  }
  
  @objc func appWillResignActive() {
    guard let window = UIApplication.shared.connectedScenes
      .compactMap({ $0 as? UIWindowScene })
      .flatMap({ $0.windows })
      .first(where: { $0.isKeyWindow }) else { return }
    
    // Önce image varsa onu göster
    if let imageUri = PrivacyProtect.config["backgroundImage"] as? String,
       let url = URL(string: imageUri),
       let data = try? Data(contentsOf: url),
       let image = UIImage(data: data) {
      
      let imageView = UIImageView(image: image)
      imageView.frame = window.bounds
      imageView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
      imageView.contentMode = .scaleAspectFill
      PrivacyProtect.imageView = imageView
      window.addSubview(imageView)
      return
    }
    
    // Blur
    let blur = UIBlurEffect(style: parseBlurStyle(PrivacyProtect.config["blurStyle"] as! String))
    let blurView = UIVisualEffectView(effect: blur)
    blurView.frame = window.bounds
    blurView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
    PrivacyProtect.blurView = blurView
    
    // Overlay
    if let color = resolveColor(PrivacyProtect.config["overlayColor"]) {
      let overlay = UIView(frame: window.bounds)
      overlay.backgroundColor = color
      overlay.autoresizingMask = [.flexibleWidth, .flexibleHeight]
      blurView.contentView.addSubview(overlay)
      PrivacyProtect.overlayView = overlay
    }
    
    // Ekle + animasyon
    if (PrivacyProtect.config["animated"] as? Bool) == true {
      blurView.alpha = 0
      window.addSubview(blurView)
      UIView.animate(withDuration: Double(PrivacyProtect.config["animationDuration"] as! Int)/1000.0) {
        blurView.alpha = 1
      }
    } else {
      window.addSubview(blurView)
    }
  }
  
  @objc func appDidBecomeActive() {
    // Image kaldır
    if let iv = PrivacyProtect.imageView {
      iv.removeFromSuperview()
      PrivacyProtect.imageView = nil
    }
    
    // Blur kaldır
    if let bv = PrivacyProtect.blurView {
      if (PrivacyProtect.config["animated"] as? Bool) == true {
        UIView.animate(withDuration: Double(PrivacyProtect.config["animationDuration"] as! Int)/1000.0,
                       animations: { bv.alpha = 0 },
                       completion: { _ in
          bv.removeFromSuperview()
          PrivacyProtect.blurView = nil
          PrivacyProtect.overlayView = nil
        })
      } else {
        bv.removeFromSuperview()
        PrivacyProtect.blurView = nil
        PrivacyProtect.overlayView = nil
      }
    }
  }
}

extension UIColor {
  convenience init?(hex: String) {
    var cleaned = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
    if cleaned.hasPrefix("#") { cleaned.remove(at: cleaned.startIndex) }
    var rgb: UInt64 = 0
    guard Scanner(string: cleaned).scanHexInt64(&rgb) else { return nil }
    if cleaned.count == 6 {
      self.init(
        red: CGFloat((rgb & 0xFF0000) >> 16) / 255.0,
        green: CGFloat((rgb & 0x00FF00) >> 8) / 255.0,
        blue: CGFloat(rgb & 0x0000FF) / 255.0,
        alpha: 1.0
      )
    } else if cleaned.count == 8 {
      self.init(
        red: CGFloat((rgb & 0xFF000000) >> 24) / 255.0,
        green: CGFloat((rgb & 0x00FF0000) >> 16) / 255.0,
        blue: CGFloat((rgb & 0x0000FF00) >> 8) / 255.0,
        alpha: CGFloat(rgb & 0x000000FF) / 255.0
      )
    } else { return nil }
  }
}
