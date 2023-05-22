import Foundation

@objc(CacheVideoModule) class CacheVideoModule: NSObject {
  @objc static func requiresMainQueueSetup() -> Bool { return true }
    @objc public func createCacheVideoModule(_ param:String){
        print("Inputs from user ",param);
  }
    @objc public func downloadVideoUsingUri(
      _ param: String
    ) -> Void {
        print("Calling downloadVideoUsingUri with URL ",param);
        DownloadEventEmitter.emitter.sendEvent(withName: "DOWNLOADING", body: ["isOkButtonPressed": true])

    }
    @objc public func isVideoAvailableForOffline(
      _ param: String
    ) -> Void {
        print("Calling isVideoAvailableForOffline with URL ",param);
    }
}
