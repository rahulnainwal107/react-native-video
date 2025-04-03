import {NativeModules} from 'react-native';

// @TODO rename to "Spec" when applying new arch
export interface NativeConvivaHelper {
    initConviva:(customerKey?: string, gatewayUrl?: string) => void;
    buildVideoAnalytics:() => void;
    reportAdLoaded:(adInfo: Record<string, unknown>) => void;
    reportAdStarted:(adInfo: Record<string, unknown>) => void;
    reportAdEnded:() => void;
    _reportAdBreakStarted:(isCSAIad:boolean, isUsingExternalPlayer:boolean) => void;
    reportAdError: (errorMessage:string) => void;
    reportAdSkipped: () => void;
    reportAdPlayPausedState: (isPaused:boolean) => void;
    reportAdBitrate:(bitrate:number) => void;
    setContentInfo:(contentInfo?:Record<string, unknown>) => void;
    _reportAdBreakEnded:() => void;
    reportPlaybackEndedFromRN:() => void;
    reportAppBackgrounded:() => void;
    reportAppForegrounded:() => void;
    reportPlaybackError:(message: string) => void;
    releaseVideoAnalyticsFromRN:() => void;
    releaseConvivaAnalytics:() => void;
    resetConvivaReloadingState: () => void;
}

export default NativeModules.ConvivaHelper as NativeConvivaHelper;
// export const ConvivaHelper = NativeModules.ConvivaHelper as ConvivaHelperTypes;
