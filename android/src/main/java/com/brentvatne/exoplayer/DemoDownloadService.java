/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brentvatne.exoplayer;

import static com.brentvatne.exoplayer.DemoUtil.DOWNLOAD_NOTIFICATION_CHANNEL_ID;

import com.brentvatne.react.CacheVideoModule;
import com.brentvatne.react.R;
import android.app.Notification;
import android.content.Context;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.PlatformScheduler;
import com.google.android.exoplayer2.scheduler.Requirements;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/** A service for downloading media. */
public class DemoDownloadService extends DownloadService {

  private static final int JOB_ID = 1;
  private static final int FOREGROUND_NOTIFICATION_ID = 1;

  public DemoDownloadService() {
    super(
        FOREGROUND_NOTIFICATION_ID,
        DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
        DOWNLOAD_NOTIFICATION_CHANNEL_ID,
        R.string.exo_download_notification_channel_name,
        /* channelDescriptionResourceId= */ 0);
  }

  @Override
  protected DownloadManager getDownloadManager() {
    // This will only happen once, because getDownloadManager is guaranteed to be called only once
    // in the life cycle of the process.
    DownloadManager downloadManager = DemoUtil.getDownloadManager(/* context= */ this);
    DownloadNotificationHelper downloadNotificationHelper =
        DemoUtil.getDownloadNotificationHelper(/* context= */ this);
    downloadManager.addListener(
        new TerminalStateNotificationHelper(
            this, downloadNotificationHelper, FOREGROUND_NOTIFICATION_ID + 1));
    return downloadManager;
  }

  @Override
  protected Scheduler getScheduler() {
    return Util.SDK_INT >= 21 ? new PlatformScheduler(this, JOB_ID) : null;
  }

  @Override
  protected Notification getForegroundNotification(
      List<Download> downloads, @Requirements.RequirementFlags int notMetRequirements) {
    return DemoUtil.getDownloadNotificationHelper(/* context= */ this)
        .buildProgressNotification(
            /* context= */ this,
            R.drawable.ic_download,
            /* contentIntent= */ null,
            /* message= */ null,
            downloads,
            notMetRequirements);
  }

  /**
   * Creates and displays notifications for downloads when they complete or fail.
   *
   * <p>This helper will outlive the lifespan of a single instance of {@link DemoDownloadService}.
   * It is static to avoid leaking the first {@link DemoDownloadService} instance.
   */
  private static final class TerminalStateNotificationHelper implements DownloadManager.Listener {

    private final Context context;
    private final DownloadNotificationHelper notificationHelper;
    private Timer timer;
    private boolean isStart;

    private static final String TAG = "MainActivity";

    private void stopTrackingProgressChanged() {
      if (isStart) {
        timer.purge();
        timer.cancel();
        isStart = false;
        timer = null;
      }
    }

    private int nextNotificationId;

    public TerminalStateNotificationHelper(
        Context context, DownloadNotificationHelper notificationHelper, int firstNotificationId) {
      this.context = context.getApplicationContext();
      this.notificationHelper = notificationHelper;
      nextNotificationId = firstNotificationId;
      this.timer = new Timer();
    }

    @Override
    public void onDownloadChanged(
        DownloadManager downloadManager, Download download, @Nullable Exception finalException) {
      Notification notification;
      Log.d("DOWNLOAD_STATE ",""+download);

      Log.d("Completed percentage ",""+download.getPercentDownloaded());
      WritableMap params = Arguments.createMap();
      if (!isStart) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
          @Override
          public void run() {
            if (downloadManager != null) {
              List<Download> currentDownloads = downloadManager.getCurrentDownloads();
              if (currentDownloads.isEmpty()) {
                stopTrackingProgressChanged();
              } else {
                for (Download download : currentDownloads) {
                  Log.d(TAG, "On download changed of " + download.request.id + " has progress " + download.getPercentDownloaded());
                  params.putDouble("progress", download.getPercentDownloaded());
                  CacheVideoModule.downloadEvent("DOWNLOADING", params.copy());
                }
              }
            }
          }
        }, 0, 2000);
        isStart = true;
      }
      if (download.state == Download.STATE_COMPLETED) {
        notification =
            notificationHelper.buildDownloadCompletedNotification(
                context,
                R.drawable.ic_download_done,
                /* contentIntent= */ null,
                Util.fromUtf8Bytes(download.request.data));
        params.putString("id", download.request.id);
        CacheVideoModule.downloadEvent("DOWNLOAD_COMPLETED", params);
        NotificationUtil.setNotification(context, nextNotificationId++, notification);
      } else if (download.state == Download.STATE_FAILED) {
        notification =
            notificationHelper.buildDownloadFailedNotification(
                context,
                R.drawable.ic_download_done,
                /* contentIntent= */ null,
                Util.fromUtf8Bytes(download.request.data));
        params.putString("id", download.request.id);
        CacheVideoModule.downloadEvent("DOWNLOAD_FAILED", params.copy());
        NotificationUtil.setNotification(context, nextNotificationId++, notification);
      } else if (download.state == Download.STATE_QUEUED) {
        params.putString("id", download.request.id);
        CacheVideoModule.downloadEvent("DOWNLOAD_QUEUED", params.copy());
      } else if (download.state == Download.STATE_STOPPED) {
        params.putString("id", download.request.id);
        CacheVideoModule.downloadEvent("DOWNLOAD_STOPPED", params.copy());
      }else if (download.state == Download.STATE_DOWNLOADING) {
//        params.putString("id", download.request.id);
//        CacheVideoModule.downloadEvent("DOWNLOADING", params);
      }else if (download.state == Download.STATE_REMOVING) {
        params.putString("id", download.request.id);
        CacheVideoModule.downloadEvent("DOWNLOAD_REMOVING", params.copy());
      }else {
        Log.d("DOWNLOAD_STATE_ELSE ",""+download);
        params.putString("id", download.request.id);
        CacheVideoModule.downloadEvent("DOWNLOAD_RESTARTING", params.copy());
        return;
      }
      //NotificationUtil.setNotification(context, nextNotificationId++, notification);
    }
  }
}
