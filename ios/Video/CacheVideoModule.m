#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_MODULE(CacheVideoModule, NSObject)
  RCT_EXTERN_METHOD(createCacheVideoModule:(NSString *)param)
RCT_EXTERN_METHOD(isVideoAvailableForOffline:(NSString *)param
                   resolver:(RCTPromiseResolveBlock)resolve
                   rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(removeDownloadVideo:(NSString *)param
                   resolver:(RCTPromiseResolveBlock)resolve
                   rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(cancelDownloadingVideoUri:(NSString *)param
                   resolver:(RCTPromiseResolveBlock)resolve
                   rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(downloadStateUri:(NSString *)param
                   resolver:(RCTPromiseResolveBlock)resolve
                   rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(downloadVideoUsingUri:(NSString *)param)
RCT_EXTERN_METHOD(removeAllDownloads)
RCT_EXTERN_METHOD(downloadManagerEmitter)
RCT_EXTERN_METHOD(restorePersistenceManager)
@end
