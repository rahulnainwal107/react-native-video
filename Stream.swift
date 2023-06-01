/*
See LICENSE folder for this sample’s licensing information.

Abstract:
A simple class that represents an entry from the `Streams.plist` file in the main application bundle.
*/

import Foundation

struct  Stream {
    
    // MARK: Properties
    
    /// The name of the stream.
    let name: String
    
    /// The URL pointing to the HLS stream.
    let playlistURL: String
    
    
}
