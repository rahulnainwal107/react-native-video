import Foundation
import AVFoundation
import UIKit

@objc(CacheVideoModule) class CacheVideoModule: UIViewController {
    @objc static func requiresMainQueueSetup() -> Bool { return true }
        @objc public func createCacheVideoModule(_ param:String){
            print("Inputs from user ",param);
      }
        @objc public func downloadVideoUsingUri(
          _ param: String
        ) -> Void {
            print("Calling downloadVideoUsingUri with URL ",param);
            //DownloadEventEmitter.emitter.sendEvent(withName: "DOWNLOADING", body: ["isOkButtonPressed": true])
            let asset = Asset(stream: Stream(name: param, playlistURL: param), urlAsset: AVURLAsset(url: URL(string: param)!))
            AssetPersistenceManager.sharedManager.downloadStream(for: asset);
            //AssetListManager.sharedManager.assets.append(asset);
        }
        @objc(isVideoAvailableForOffline:resolver:rejecter:) public func isVideoAvailableForOffline(
          _ param: String, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock
        ) -> Void {
            print("Calling isVideoAvailableForOffline with URL ",param);
            let asset = Asset(stream: Stream(name: param, playlistURL: param), urlAsset: AVURLAsset(url: URL(string: param)!))
            let downloadState = AssetPersistenceManager.sharedManager.downloadState(for: asset);
            if(downloadState.rawValue == Asset.DownloadState.downloaded.rawValue){
                return resolve(true);
            }            else{
                return resolve(false);
            }
            
        }
        @objc public func removeAllDownloads() -> Void {
            print("Calling removeAllDownloads ");
//            let cachePath = NSSearchPathForDirectoriesInDomains(.cachesDirectory, .userDomainMask, true)[0]
//            //let playerCachePath = cachePath + "/com.apple.avplayer/"
//            let playerCachePath = cachePath
//
//              do {
//                let fileManager = FileManager.default
//                try fileManager.removeItem(atPath: playerCachePath)
//                print("AVPlayer offline data cleared successfully.")
//              } catch {
//                print("Failed to clear AVPlayer offline data: \(error.localizedDescription)")
//              }
            // Clearing all app-specific data
            clearAppCache()
            clearUserDefaults()
        }
    
    // Clearing app cache data
    func clearAppCache() {
        URLCache.shared.removeAllCachedResponses()
    }

    // Clearing app user defaults
    func clearUserDefaults() {
        if let bundleIdentifier = Bundle.main.bundleIdentifier {
            UserDefaults.standard.removePersistentDomain(forName: "org.reactjs.native.example.ExoPlayPOC")
        }
    }

        @objc public func downloadManagerEmitter() -> Void {
            print("Calling downloadManagerEmitter ");
        }
    @objc public func restorePersistenceManager()->Void{
        print("Calling restorePersistenceManager ");
        AssetPersistenceManager.sharedManager.restorePersistenceManager()
    }
}
