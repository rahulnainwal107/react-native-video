package com.brentvatne.react;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.brentvatne.exoplayer.DemoUtil;
import com.brentvatne.exoplayer.DownloadTracker;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class CacheVideoModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "CacheVideoModule";
    private final Context context;
    private static ReactApplicationContext reactContext = null;
    private FragmentManager fragmentManager;
    private MenuItem preferExtensionDecodersMenuItem;
    private static @MonotonicNonNull File downloadDirectory;
    private static @MonotonicNonNull DatabaseProvider databaseProvider;
    private static DataSource.@MonotonicNonNull Factory httpDataSourceFactory;
    private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";
    private static @MonotonicNonNull Cache downloadCache;
    public static String videoUri = null;
    public static String videoTitle = null;
    CacheVideoModule(ReactApplicationContext context) {
        super(context);
        this.context = context;
        this.reactContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void createCacheVideoModule(String name, String location) {
        Log.d("CacheVideoModule", "Create event called with name: " + name
                + " and location: " + location);
    }

    @ReactMethod
    public void downloadVideoUsingUri(String  videoUri,String title){
        //ExoPlayer player = new ExoPlayer.Builder(context).build();
        Log.d("Video Uri ", "" + videoUri);
        this.videoUri = videoUri;
        this.videoTitle = title;
        onSampleDownloadButtonClicked(videoUri);
    }

    @ReactMethod
    public void isVideoAvailableForOffline(String videoUri, Promise promise){
        try {
            DownloadTracker downloadTracker = DemoUtil.getDownloadTracker(/* context= */ context);
            boolean isDownloaded = downloadTracker.isDownloaded(MediaItem.fromUri(videoUri));
            Log.d("isDownloaded ",""+isDownloaded);
            promise.resolve(isDownloaded);
        } catch (Exception e) {
            promise.reject("ERROR_CODE");
        }
    }

    @ReactMethod
    public void getAbsolutePath(String uri){
//       DataSource.Factory downloadTracker = DemoUtil.getHttpDataSourceFactory(/* context= */ context);
//       MediaSource videoSource = new ProgressiveMediaSource.Factory(downloadTracker).createMediaSource(MediaItem.fromUri(uri));
//
//       //player.prepare(videoSource);
//       Log.d("videoSource =>",""+videoSource.getMediaItem().localConfiguration.uri.getPath());
//
//       MediaItem mediaItem = videoSource.getMediaItem();
//       Uri videoUri = mediaItem.localConfiguration.uri;
//
//// Get the file path of the video
//       File videoFile = new File(videoUri.getPath());
//       Log.d("videoFile =>",""+videoFile);
//       String videoFilePath = videoFile.getAbsolutePath();
//       Log.d("videoFilePath =>",""+videoFilePath);
//
//// Get the base path of your app's files directory
//       File appFilesDir = context.getFilesDir();
//       Log.d("appFilesDir =>",""+appFilesDir);
//       String appFilesPath = appFilesDir.getAbsolutePath();
//       Log.d("appFilesPath =>",""+appFilesPath);
//
//// Remove the base path from the video file path to get the relative path
//       String relativeVideoPath = videoFilePath.replace(appFilesPath, "");
//       Log.d("relativeVideoPath =>",""+relativeVideoPath);
//// Use the relative path as needed
//       Log.d(TAG, "Relative video path: " + appFilesDir+"/downloads"+relativeVideoPath);
//       File file = new File(relativeVideoPath);
//       if(file.exists()){
//           Log.d("File","True");
//       }else{
//           Log.d("File","False");
//       }


        //2
//       Cache downloadCache = DemoUtil.getDownloadCache(/* context= */ context);
//       DataSource.Factory cacheDataSourceFactory =
//               new CacheDataSource.Factory()
//                       .setCache(downloadCache)
//                       .setUpstreamDataSourceFactory(httpDataSourceFactory)
//                       .setCacheWriteDataSinkFactory(null); // Disable writing.
//       DownloadTracker downloadTracker = DemoUtil.getDownloadTracker(/* context= */ context);
//       DownloadRequest downloadRequest = downloadTracker.getDownloadRequest(Uri.parse(uri));
//       MediaItem m= downloadRequest.toMediaItem();
//       Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//       Log.d("<M<<>> N","download"+gson.toJson(m));

//       DataSource.Factory getDataSourceFactory = DemoUtil.getDataSourceFactory(context);
//       ProgressiveMediaSource mediaSource =
//               new ProgressiveMediaSource.Factory(getDataSourceFactory)
//                       .createMediaSource(MediaItem.fromUri(uri));
//
//       Log.d("mediaSource ","download"+mediaSource);

        // Create an instance of ExoPlayer
//       ExoPlayer player = new ExoPlayer.Builder(context).build();
//// Set the playWhenReady flag to true
//       player.setPlayWhenReady(true);
//// Get the media source from the DownloadTracker
//       DownloadTracker downloadTracker = DemoUtil.getDownloadTracker(/* context= */ context);
//       DownloadRequest downloadRequest = downloadTracker.getDownloadRequest(Uri.parse(uri));
//       Gson gson = new GsonBuilder().setPrettyPrinting().create();
//       Log.d("downloadRequest ", gson.toJson(downloadRequest));
//       MediaSource mediaSource = DownloadHelper.createMediaSource(downloadRequest, DemoUtil.getDataSourceFactory(context));
//// Set the media source to the player and prepare it
//       player.setMediaSource(mediaSource);
//       player.prepare();
//       Uri url = Uri.parse(uri);
//       String videoId = url.getQueryParameter("videoId");
//       Log.d("videoId",""+videoId+""+downloadRequest);



    }

    @ReactMethod
    public static void downloadEvent(String eventName, WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @ReactMethod
    public void removeAllDownloads(){
        DownloadManager downloadManager = DemoUtil.getDownloadManager(context);
        downloadManager.removeAllDownloads();
    }

    private static synchronized DatabaseProvider getDatabaseProvider(Context context) {
        if (databaseProvider == null) {
            databaseProvider = new StandaloneDatabaseProvider(context);
        }
        return databaseProvider;
    }

    private static synchronized File getDownloadDirectory(Context context) {
        if (downloadDirectory == null) {
            downloadDirectory = context.getExternalFilesDir(/* type= */ null);
            if (downloadDirectory == null) {
                downloadDirectory = context.getFilesDir();
            }
        }
        return downloadDirectory;
    }

    private void onSampleDownloadButtonClicked(String videoUri) {
//        int downloadUnsupportedStringId = getDownloadUnsupportedStringId(playlistHolder);
//        if (downloadUnsupportedStringId != 0) {
//            Toast.makeText(context, downloadUnsupportedStringId, Toast.LENGTH_LONG)
//                    .show();
//        } else if (!notificationPermissionToastShown
//                && Util.SDK_INT >= 33
//                && checkSelfPermission(Api33.getPostNotificationPermissionString())
//                != PackageManager.PERMISSION_GRANTED) {
//            downloadMediaItemWaitingForNotificationPermission = MediaItem.fromUri(playlistHolder.getUri());
//            requestPermissions(
//                    new String[] {Api33.getPostNotificationPermissionString()},
//                    /* requestCode= */ POST_NOTIFICATION_PERMISSION_REQUEST_CODE);
//        } else {
//            toggleDownload(MediaItem.fromUri(playlistHolder.getUri()),context);
//        }
        toggleDownload(MediaItem.fromUri(videoUri));
        Log.d("Button Press222 ",""+videoUri);
    }

    private void toggleDownload(MediaItem mediaItem) {
        Log.d("Button toggleDownload ","");
        RenderersFactory renderersFactory =
                DemoUtil.buildRenderersFactory(
                        /* context= */ context, isNonNullAndChecked(preferExtensionDecodersMenuItem));
        DownloadTracker downloadTracker = DemoUtil.getDownloadTracker(/* context= */ context);
        Activity activity = getCurrentActivity();
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            // Use the fragmentManager here...
            Log.d("Button fragmentManager ",""+fragmentManager);
            downloadTracker.toggleDownload(fragmentManager, mediaItem, renderersFactory);
        }
    }

    private static boolean isNonNullAndChecked(@Nullable MenuItem menuItem) {
        // Temporary workaround for layouts that do not inflate the options menu.
        return menuItem != null && menuItem.isChecked();
    }


}