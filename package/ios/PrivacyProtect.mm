#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(PrivacyProtect, NSObject)
RCT_EXTERN_METHOD(configure:(NSDictionary *)options)
@end

@implementation PrivacyProtect (AutoLoad)

+ (void)load {
  static PrivacyProtect *instance;
  instance = [PrivacyProtect new];
}

@end
