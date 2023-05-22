import Foundation
import React

@objc(DownloadEventEmitter)
open class DownloadEventEmitter: RCTEventEmitter {
    @objc override public static func requiresMainQueueSetup() -> Bool { return true }
  public static var emitter: RCTEventEmitter!

  override init() {
    super.init()
      DownloadEventEmitter.emitter = self
  }

  open override func supportedEvents() -> [String] {
      ["DOWNLOAD_COMPLETED", "DOWNLOAD_FAILED", "DOWNLOAD_QUEUED","DOWNLOAD_STOPPED","DOWNLOADING","DOWNLOAD_REMOVING","DOWNLOAD_RESTARTING","OK_BUTTON_PRESS","CANCEL_BUTTON_PRESS"]      // etc.
  }
}
