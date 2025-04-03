//
//  RCTConvivaHelper.swift
//  Pods
//
//  Created by Rahul Nainwal on 06/04/24.
//

import AVFoundation
import React
import ConvivaSDK
import GoogleInteractiveMediaAds

@objc(ConvivaHelper)
class RCTConvivaHelper: RCTViewManager {
    // Static property to hold the analytics instance
    static var analytics:CISAnalytics?;
    static var addEnabled:Bool = false;
    static var videoAnalytics:CISVideoAnalytics?;
    static var videoAdAnalytics:CISAdAnalytics?;

    @objc(initConviva:gatewayUrl:)
    func initConviva(customerKey: String, gatewayUrl: String?) {
        print("The value of variable is: \(customerKey) \(String(describing: gatewayUrl))")
#if DEBUG
        do {
            var settings: NSDictionary = [:];
            if (gatewayUrl != nil){
                settings = [CIS_SSDK_SETTINGS_GATEWAY_URL:gatewayUrl!, CIS_SSDK_SETTINGS_LOG_LEVEL:LogLevel.LOGLEVEL_DEBUG.rawValue]
            }else{
                settings = [CIS_SSDK_SETTINGS_LOG_LEVEL:LogLevel.LOGLEVEL_DEBUG.rawValue]
            }
            let swiftSettings = settings as? [AnyHashable: Any]
            RCTConvivaHelper.analytics = CISAnalyticsCreator.create(withCustomerKey: customerKey , settings:swiftSettings)
        }
#else
        RCTConvivaHelper.analytics = CISAnalyticsCreator.create(withCustomerKey: customerKey)!
#endif
    }

    @objc
    func buildVideoAnalytics() {
        print("buildVideoAnalytics: ")
        if(RCTConvivaHelper.analytics != nil){
            RCTConvivaHelper.videoAnalytics = RCTConvivaHelper.analytics!.createVideoAnalytics()
        }
    }

    func _buildVideoAnalytics() -> CISVideoAnalytics? {
        if (RCTConvivaHelper.analytics != nil && RCTConvivaHelper.videoAnalytics == nil) {
            RCTConvivaHelper.videoAnalytics = RCTConvivaHelper.analytics!.createVideoAnalytics()
            UserDefaults.standard.set(true, forKey: "reloadingPlayer"); 
        }
        buildAdAnalytics();
        return RCTConvivaHelper.videoAnalytics;
    }
    
    func buildAdAnalytics() -> CISAdAnalytics? {
    print("buildAdAnalytics 1")
    
    do {
        if RCTConvivaHelper.analytics != nil &&
           RCTConvivaHelper.videoAnalytics != nil &&
           RCTConvivaHelper.videoAdAnalytics == nil {
           
            print("buildAdAnalytics 2 if")
            
            // Attempt to create Ad Analytics
            RCTConvivaHelper.videoAdAnalytics = try RCTConvivaHelper.analytics!.createAdAnalytics(
                withVideoAnalytics: RCTConvivaHelper.videoAnalytics!
            )
        }
    } catch {
        print("Error in buildAdAnalytics: \(error.localizedDescription)")
        return nil // Return nil or handle appropriately
    }
    
    print("END of llll")
    return RCTConvivaHelper.videoAdAnalytics
}
    
//    func enableAdMetricAndMetadataDetection(adTagUrl:String,isUsingExternalPlayer:Bool, adsLoader:IMAAdsLoader){
//        print("enableAdMetricAndMetadataDetection - -- ")
//        var info = [String: Any]()
//        if(RCTConvivaHelper.videoAdAnalytics != nil && adTagUrl != nil && adsLoader != nil){
//                info[CIS_SSDK_METADATA_AD_TAG_URL] = adTagUrl;
//                if (isUsingExternalPlayer == true) {
//                    info[CIS_SSDK_AD_BREAK_AD_PLAYER] = AdPlayer.ADPLAYER_SEPARATE;
//                }else{
//                    info[CIS_SSDK_AD_BREAK_AD_PLAYER] = AdPlayer.ADPLAYER_CONTENT;
//                }
//            let proxy = CISIMAProxy.createIMAProxy(adsLoader);
//            RCTConvivaHelper.videoAdAnalytics!.setAdListener(proxy, andInfo:info);
//            print("enableAdMetricAndMetadataDetection ended - -- ")
//        }else{
//            print("adsLoader issue - -- ")
//        }
//    }

    @objc(setPlayerInfo:)
    func setPlayerInfo(_ playerInfo: NSDictionary?) {
        if let videoAnalytics = RCTConvivaHelper.videoAnalytics, let playerInfo = playerInfo as? [String: Any] {
            videoAnalytics.setPlayerInfo(playerInfo)
        } else {
            NSLog("playerInfo conviva setPlayerInfo: playerInfo is null or not of type [String: Any]")
        }
    }

    func _setPlayerInfo() {
        if let videoAnalytics = RCTConvivaHelper.videoAnalytics {
            videoAnalytics.setPlayerInfo([CIS_SSDK_METADATA_PLAYER_NAME: "sooka iOS" ])
        } else {
            NSLog("playerInfo conviva setPlayerInfo: playerInfo is null or not of type [String: Any]")
        }
    }

    @objc(setContentInfo:)
    func setContentInfo(_ _contentInfo: NSDictionary?) {
        if let videoAnalytics = RCTConvivaHelper.videoAnalytics, let contentInfo = _contentInfo as? [String: Any] {
            videoAnalytics.setContentInfo(contentInfo)
        } else {
            NSLog("playerInfo conviva setPlayerInfo: playerInfo is null or not of type [String: Any]")
        }
    }

    func setContentInfo(_ contentInfo: [String: Any]?) {
        if let videoAnalytics = RCTConvivaHelper.videoAnalytics, let contentInfo = contentInfo {
            videoAnalytics.setContentInfo(contentInfo)
        } else {
            NSLog("contentInfo conviva setContentInfo: contentInfo is nil")
        }
   }

    func reportPlaybackRequested(_ videoAnalytics: CISVideoAnalytics?, contentInfo: [String: Any]?) {
        if let contentInfo = contentInfo {
            videoAnalytics?.reportPlaybackRequested(contentInfo)
        } else {
            NSLog("contentInfo conviva setContentInfo: contentInfo is nil")
        }
    }

    func setPlayerReference(_ videoAnalytics: CISVideoAnalytics?, newPlayer: Any?, convivaContentInfo: [String: Any]?) {
        guard let videoAnalytics = videoAnalytics, let newPlayer = newPlayer else {
            NSLog("videoAnalytics or newPlayer is nil")
            return
        }
        _setPlayerInfo()
        setContentInfo(convivaContentInfo)
        videoAnalytics.setPlayer(newPlayer)
        reportPlaybackRequested(videoAnalytics, contentInfo: convivaContentInfo)
//        enableAdMetricAndMetadataDetection(adTagUrl: adTagUrl, isUsingExternalPlayer: false, adsLoader: adsLoader)
    }

    @objc(reportAdBreakStarted:adType:adAtrributes:)
    func reportAdBreakStarted(_ adPlayer: ConvivaSDK.AdPlayer, adType: ConvivaSDK.AdTechnology,adAtrributes:[String: Any]?) {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }
        // TODO - Need to fix when we will required to handle ads
        //videoAnalytics.reportAdBreakStarted(AdPlayer.ADPLAYER_SEPARATE, adType: AdTechnology.CLIENT_SIDE, adBreakInfo: adAtrributes ?? nil)
    }

    func reportSeekStarted(value:Float){
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }
        videoAnalytics.reportPlaybackMetric(CIS_SSDK_PLAYBACK_METRIC_SEEK_STARTED, value: value * 1000)
    }

    func reportSeekEnded(value:Float){
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }
        videoAnalytics.reportPlaybackMetric(CIS_SSDK_PLAYBACK_METRIC_SEEK_ENDED, value: value * 1000)
    }

    @objc(reportAdBreakEnded)
    func reportAdBreakEnded() {
        print("reportAdBreakEnded ")
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }

        videoAnalytics.reportAdBreakEnded()
    }
    
    @objc(_reportAdBreakStarted:isUsingExternalPlayer:)
    func _reportAdBreakStarted(isCSAIad:NSNumber, isUsingExternalPlayer:NSNumber){
        print("_reportAdBreakStarted ",isCSAIad.boolValue,isUsingExternalPlayer.boolValue)
//        let videoAnalytics = _buildVideoAnalytics();
        if(RCTConvivaHelper.videoAnalytics != nil){
            if(isUsingExternalPlayer.boolValue == true && isCSAIad.boolValue == true){
                RCTConvivaHelper.videoAnalytics!.reportAdBreakStarted(AdPlayer.ADPLAYER_SEPARATE, adType: AdTechnology.CLIENT_SIDE, adBreakInfo: [ : ] );
            }else if(isUsingExternalPlayer.boolValue == true && isCSAIad.boolValue == false){
                RCTConvivaHelper.videoAnalytics!.reportAdBreakStarted(AdPlayer.ADPLAYER_SEPARATE, adType: AdTechnology.SERVER_SIDE,adBreakInfo:[:]);
            }else if(isUsingExternalPlayer.boolValue == false && isCSAIad.boolValue == true) {
                RCTConvivaHelper.videoAnalytics!.reportAdBreakStarted(AdPlayer.ADPLAYER_CONTENT, adType: AdTechnology.CLIENT_SIDE,adBreakInfo:[:]);
            }else{
                RCTConvivaHelper.videoAnalytics!.reportAdBreakStarted(AdPlayer.ADPLAYER_CONTENT, adType: AdTechnology.SERVER_SIDE,adBreakInfo:[:]);
            }
        }else{
            print(" sms no")
        }
    }
    
    @objc(_reportAdBreakEnded)
    func _reportAdBreakEnded() {
        print("_reportAdBreakEnded");
        if (RCTConvivaHelper.videoAnalytics != nil) {
            RCTConvivaHelper.videoAnalytics!.reportAdBreakEnded();
             }
         }
    
    @objc(reportAdError:)
    func reportAdError(errorMessage : String) {
        print("reportAdError ")
            if(RCTConvivaHelper.videoAdAnalytics != nil){
                RCTConvivaHelper.videoAdAnalytics!.reportAdError(errorMessage, severity: ErrorSeverity.ERROR_FATAL);
            }
         }
    
    @objc(reportAdSkipped)
    func reportAdSkipped(){
        print("reportAdSkipped ")
        if(RCTConvivaHelper.videoAdAnalytics != nil){
            RCTConvivaHelper.videoAdAnalytics!.reportAdSkipped();
        }
    }
    
    @objc(reportAdPlayPausedState:)
    func reportAdPlayPausedState(isPaused:NSNumber){
        print("reportAdPlayPausedState ")
        if(RCTConvivaHelper.videoAdAnalytics != nil){
            if(isPaused == true){
                RCTConvivaHelper.videoAdAnalytics?.reportAdMetric(CIS_SSDK_PLAYBACK_METRIC_PLAYER_STATE, value: PlayerState.CONVIVA_PAUSED.rawValue);
            }else{
                RCTConvivaHelper.videoAdAnalytics?.reportAdMetric(CIS_SSDK_PLAYBACK_METRIC_PLAYER_STATE, value: PlayerState.CONVIVA_PLAYING.rawValue);
            }
        }
    }
    
    @objc(reportAdBitrate:)
    func reportAdBitrate(bitrate:NSNumber){
        print("reportAdBitrate ")
        if(RCTConvivaHelper.videoAdAnalytics != nil){
            RCTConvivaHelper.videoAdAnalytics?.reportAdMetric(CIS_SSDK_PLAYBACK_METRIC_BITRATE, value: bitrate);
        }
    }
    
    @objc(reportAdMetric:_value:)
    func reportAdMetric(_key:String,_value:Any){
        if(RCTConvivaHelper.videoAdAnalytics != nil){
            RCTConvivaHelper.videoAdAnalytics?.reportAdMetric(_key, value: _value)
        }
    }
    
    @objc(reportAdLoaded:)
    func reportAdLoaded(_ adInfo:NSDictionary?){
        if let videoAdAnalytics = RCTConvivaHelper.videoAdAnalytics, var adInfo = adInfo as? [String: Any] {
            adInfo[CIS_SSDK_PLAYER_FRAMEWORK_NAME] = "Google IMA SDK";
            adInfo[CIS_SSDK_PLAYER_FRAMEWORK_VERSION] = "3.23.0";
            adInfo[CIS_SSDK_METADATA_PLAYER_NAME] = "sooka iOS";
            videoAdAnalytics.reportAdLoaded(adInfo)
            RCTConvivaHelper.addEnabled = true;
        }
    }
    
    @objc(reportAdStarted:)
    func reportAdStarted(_ adInfo:NSDictionary?){
        if let videoAdAnalytics = RCTConvivaHelper.videoAdAnalytics, var adInfo = adInfo as? [String: Any] {
            adInfo[CIS_SSDK_PLAYER_FRAMEWORK_NAME] = "Google IMA SDK";
            adInfo[CIS_SSDK_PLAYER_FRAMEWORK_VERSION] = "3.23.0";
            adInfo[CIS_SSDK_METADATA_PLAYER_NAME] = "sooka iOS";
            videoAdAnalytics.reportAdStarted(adInfo)
        }
    }
    
    @objc(reportAdEnded)
    func reportAdEnded(){
        print("reportAdEnded ")
        if(RCTConvivaHelper.videoAdAnalytics != nil){
            RCTConvivaHelper.videoAdAnalytics?.reportAdEnded();
            RCTConvivaHelper.addEnabled = false;
        }
    }
    
    func _reportAdEnded(){
        print("reportAdEnded ")
        if(RCTConvivaHelper.videoAdAnalytics != nil){
            RCTConvivaHelper.videoAdAnalytics?.reportAdEnded();
            RCTConvivaHelper.addEnabled = false;
        }
    }
    

    @objc(reportPlaybackEndedFromRN)
    func reportPlaybackEndedFromRN() {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }

        videoAnalytics.reportPlaybackEnded()
    }

    func reportPlaybackEnded() {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }

        videoAnalytics.reportPlaybackEnded()
    }

    @objc(reportPlaybackEventWaitStarted)
    func reportPlaybackEventWaitStarted() {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }
        let eventValue = "\(ConvivaSDK.Events.USER_WAIT_STARTED.rawValue)"
        videoAnalytics.reportPlaybackEvent(eventValue)
    }

    @objc(reportPlaybackEventWaitEnded)
    func reportPlaybackEventWaitEnded() {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }

        let eventValue = "\(ConvivaSDK.Events.USER_WAIT_ENDED.rawValue)"
        videoAnalytics.reportPlaybackEvent(eventValue)
    }

    @objc(reportPlaybackFailed:contentInfo:)
    func reportPlaybackFailed(_ errorMessage: String, contentInfo: [String: Any]?) {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }

        videoAnalytics.reportPlaybackFailed(errorMessage, contentInfo: contentInfo)
    }

    @objc(reportPlaybackEvent:eventDetail:)
    func reportPlaybackEvent(_ eventType: String, eventDetail: [String: Any]?) {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }

        videoAnalytics.reportPlaybackEvent(eventType)
    }

    @objc(releaseVideoAnalyticsFromRN)
    func releaseVideoAnalyticsFromRN() {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }

        videoAnalytics.cleanup()
        RCTConvivaHelper.videoAnalytics = nil
    }

    func releaseVideoAnalytics() {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            NSLog("videoAnalytics is nil")
            return
        }

        videoAnalytics.cleanup()
        RCTConvivaHelper.videoAnalytics = nil
    }
    
    func releaseVideoAdAnalytics() {
        if (RCTConvivaHelper.videoAdAnalytics != nil) {
            RCTConvivaHelper.videoAdAnalytics!.cleanup();
                 RCTConvivaHelper.videoAdAnalytics = nil;
             }
         }

    @objc(releaseConvivaAnalytics)
    func releaseConvivaAnalytics() {
        RCTConvivaHelper.videoAnalytics = nil
        RCTConvivaHelper.analytics?.cleanup()
        RCTConvivaHelper.analytics = nil
    }

     @objc(resetConvivaReloadingState)
    func resetConvivaReloadingState() {
        let isReloadingPlayer = UserDefaults.standard.bool(forKey: "reloadingPlayer");
        if(isReloadingPlayer == true) {
            UserDefaults.standard.set(false, forKey: "reloadingPlayer");
            if(RCTConvivaHelper.videoAnalytics != nil){
                self._reportAdEnded()
                self.releaseVideoAdAnalytics()
                self.reportPlaybackEnded()
                self.releaseVideoAnalytics()
            }
        }
    }

    @objc(reportPlaybackError:)
    func reportPlaybackError(_ message: String) {
        guard let videoAnalytics = RCTConvivaHelper.videoAnalytics else {
            return
        }
        videoAnalytics.reportPlaybackError(message,errorSeverity: .ERROR_FATAL)
    }
    
    @objc(reportAppBackgrounded)
    func reportAppBackgrounded() {
        guard let analytics = RCTConvivaHelper.analytics else {
            return
        }
        analytics.reportAppBackgrounded()
    }
    
    @objc(reportAppForegrounded)
    func reportAppForegrounded() {
        guard let analytics = RCTConvivaHelper.analytics else {
            return
        }
        analytics.reportAppForegrounded()
    }
}
