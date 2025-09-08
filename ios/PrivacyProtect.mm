#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(PrivacyProtect, NSObject)
@end

@implementation PrivacyProtect (AutoLoad)

+ (void)load {
  // Swift sınıfını elle init et → böylece observer kaydolur
  static PrivacyProtect *instance;
  instance = [PrivacyProtect new];
}

@end
