//
//  RCTConvivaHelper.m
//  Pods
//
//  Created by Rahul Nainwal on 06/04/24.
//

#import <React/RCTBridge.h>
#import "React/RCTViewManager.h"

@interface RCT_EXTERN_MODULE(ConvivaHelper, RCTViewManager)

RCT_EXTERN_METHOD(initConviva:(NSString *)customerKey gatewayUrl:(NSString *)gatewayUrl)
RCT_EXTERN_METHOD(buildVideoAnalytics)
//RCT_EXTERN_METHOD(setPlayerInfo:(NSDictionary *) playerInfo)
RCT_EXTERN_METHOD(setContentInfo:(NSDictionary *) _contentInfo)
//RCT_EXTERN_METHOD(reportAdBreakStarted:(NSString *) adPlayer adType:(NSString *)adType adAtrributes:(NSDictionary *) adAtrributes)
//RCT_EXTERN_METHOD(reportAdBreakEnded)
RCT_EXTERN_METHOD(reportPlaybackEndedFromRN)
//RCT_EXTERN_METHOD(reportPlaybackEventWaitStarted)
//RCT_EXTERN_METHOD(reportPlaybackEventWaitEnded)
RCT_EXTERN_METHOD(reportAppBackgrounded)
RCT_EXTERN_METHOD(reportAppForegrounded)
//RCT_EXTERN_METHOD(reportPlaybackFailed:(NSString *) errorMessage contentInfo:(NSDictionary *) contentInfo)
//RCT_EXTERN_METHOD(reportPlaybackEvent:(NSString *) eventType eventDetail:(NSDictionary *) eventDetail)
RCT_EXTERN_METHOD(releaseVideoAnalyticsFromRN)
//RCT_EXTERN_METHOD(releaseVideoAnalytics)
RCT_EXTERN_METHOD(releaseConvivaAnalytics)
RCT_EXTERN_METHOD(reportPlaybackError:(NSString *) errorMessage)
RCT_EXTERN_METHOD(_reportAdBreakEnded)
RCT_EXTERN_METHOD(_reportAdBreakStarted:(nonnull NSNumber *) isCSAIad isUsingExternalPlayer:(nonnull NSNumber *) isUsingExternalPlayer)
//RCT_EXTERN_METHOD(_reportAdBreakEnded)
RCT_EXTERN_METHOD(reportAdError:(NSString *) errorMessage)
RCT_EXTERN_METHOD(reportAdSkipped)
RCT_EXTERN_METHOD(reportAdPlayPausedState:(nonnull NSNumber *) isPaused)
RCT_EXTERN_METHOD(reportAdBitrate:(nonnull NSNumber *) bitrate)
RCT_EXTERN_METHOD(reportAdLoaded:(NSDictionary *) adInfo)
RCT_EXTERN_METHOD(reportAdStarted:(NSDictionary *) adInfo)
RCT_EXTERN_METHOD(reportAdEnded)
RCT_EXTERN_METHOD(resetConvivaReloadingState)
@end

