/**
 * Written by Rahul Nainwal
 */

import NativeConvivaHelper from './specs/NativeConvivaHelper';

export const ConvivaHelper = {
  initConviva(...args: Parameters<typeof NativeConvivaHelper.initConviva>) {
    return NativeConvivaHelper.initConviva(...args);
  },
  buildVideoAnalytics(){
    return NativeConvivaHelper.buildVideoAnalytics();
  },
  reportAdLoaded(...args: Parameters<typeof NativeConvivaHelper.reportAdLoaded>){
    return NativeConvivaHelper.reportAdLoaded(...args);
  },
  reportAdStarted(...args: Parameters<typeof NativeConvivaHelper.reportAdStarted>){
    return NativeConvivaHelper.reportAdStarted(...args);
  },
  reportAdEnded(){
    return NativeConvivaHelper.reportAdEnded();
  },
  _reportAdBreakStarted(...args: Parameters<typeof NativeConvivaHelper._reportAdBreakStarted>){
    return NativeConvivaHelper._reportAdBreakStarted(...args);
  },
  reportAdError(...args: Parameters<typeof NativeConvivaHelper.reportAdError>){
    return NativeConvivaHelper.reportAdError(...args);
  },
  reportAdSkipped(){
    return NativeConvivaHelper.reportAdSkipped();
  },
  reportAdPlayPausedState(...args: Parameters<typeof NativeConvivaHelper.reportAdPlayPausedState>){
    return NativeConvivaHelper.reportAdPlayPausedState(...args);
  },
  reportAdBitrate(...args: Parameters<typeof NativeConvivaHelper.reportAdBitrate>){
    return NativeConvivaHelper.reportAdBitrate(...args);
  },
  setContentInfo(...args: Parameters<typeof NativeConvivaHelper.setContentInfo>){
    return NativeConvivaHelper.setContentInfo(...args);
  },
  _reportAdBreakEnded(){
    return NativeConvivaHelper._reportAdBreakEnded();
  },
  reportPlaybackEndedFromRN(){
    return NativeConvivaHelper.reportPlaybackEndedFromRN();
  },
  reportAppBackgrounded(){
    return NativeConvivaHelper.reportAppBackgrounded();
  },
  reportAppForegrounded(){
    return NativeConvivaHelper.reportAppForegrounded();
  },
  reportPlaybackError(...args: Parameters<typeof NativeConvivaHelper.reportPlaybackError>){
    return NativeConvivaHelper.reportPlaybackError(...args);
  },
  releaseVideoAnalyticsFromRN(){
    return NativeConvivaHelper.releaseVideoAnalyticsFromRN();
  },
  releaseConvivaAnalytics(){
    return NativeConvivaHelper.releaseConvivaAnalytics();
  },
  resetConvivaReloadingState(){
    return NativeConvivaHelper.resetConvivaReloadingState();
  },
};
