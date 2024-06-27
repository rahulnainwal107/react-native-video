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
        @objc(cancelDownloadingVideoUri:resolver:rejecter:) public func cancelDownloadingVideoUri(
          _ param: String, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock
        ) -> Void {
            print("Calling cancelDownloadingVideoUri with URL ",param);
            let asset = Asset(stream: Stream(name: param, playlistURL: param), urlAsset: AVURLAsset(url: URL(string: param)!))
            AssetPersistenceManager.sharedManager.cancelDownload(for: asset);
            print("Calling cancelDownloadingVideoUri with downloadState:: ");
           return resolve(true)
            
        }
        @objc(downloadStateUri:resolver:rejecter:) public func downloadStateUri(
          _ param: String, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock
        ) -> Void {
            print("Calling downloadStateUri with URL ",param);
            let asset = Asset(stream: Stream(name: param, playlistURL: param), urlAsset: AVURLAsset(url: URL(string: param)!))
            let downloadState = AssetPersistenceManager.sharedManager.downloadState(for: asset);
            return resolve(downloadState.rawValue);
            
        }
        @objc(removeDownloadVideo:resolver:rejecter:) public func removeDownloadVideo(
          _ param: String, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock
        ) -> Void {
            print("Calling removeDownloadVideo with URL ",param);
            let asset = Asset(stream: Stream(name: param, playlistURL: param), urlAsset: AVURLAsset(url: URL(string: param)!))
            AssetPersistenceManager.sharedManager.deleteAsset(asset);
            return resolve(true);
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
