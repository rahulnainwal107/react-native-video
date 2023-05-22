#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_MODULE(CacheVideoModule, NSObject)
  RCT_EXTERN_METHOD(createCacheVideoModule:(NSString *)param)
RCT_EXTERN_METHOD(isVideoAvailableForOffline:(NSString *)param)
RCT_EXTERN_METHOD(downloadVideoUsingUri:(NSString *)param)
@end
