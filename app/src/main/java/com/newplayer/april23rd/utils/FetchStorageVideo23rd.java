package com.newplayer.april23rd.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.newplayer.april23rd.R;
import com.newplayer.april23rd.model.GalleryVideoFolderDetails23rd;
import com.newplayer.april23rd.model.GalleryVideoFolderInformation23rd;
import com.newplayer.april23rd.model.GalleryVideoInformation23rd;
import com.newplayer.april23rd.model.MusicFile23rd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FetchStorageVideo23rd {
    public static int ShowTipsScreen = 0;
    public static String SELECTED_VIDEO_POSITION = "SELECTED_VIDEO_POSITION";
    public static String DATA_LIST = "DATA_LIST";

    public static ArrayList<GalleryVideoFolderDetails23rd> getGalleryVideoFolders(Context context) {
        ArrayList<GalleryVideoFolderDetails23rd> videoFolders = new ArrayList<>();
        HashMap<String, GalleryVideoFolderInformation23rd> folderInfoMap = new HashMap<>();
        HashSet<String> folderSet = new HashSet<>();

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.RESOLUTION
        };
        String sortOrder = MediaStore.Video.Media.TITLE + " DESC";
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(videoUri, projection, null, null, sortOrder);

        if (cursor != null) {
            int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
            int durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
            int dateAddedIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
            int resolutionIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION);

            while (cursor.moveToNext()) {
                String filePath = cursor.getString(dataIndex);
                long fileSize = cursor.getLong(sizeIndex);
                String title = cursor.getString(titleIndex);
                long duration = cursor.getLong(durationIndex);
                String bucketDisplayName = cursor.getString(bucketDisplayNameIndex);
                long dateAdded = cursor.getLong(dateAddedIndex);
                String resolution = cursor.getString(resolutionIndex);

                filePath = (filePath != null) ? filePath : "info not found";
                title = (title != null) ? title : "info not found";
                bucketDisplayName = (bucketDisplayName != null) ? bucketDisplayName : "info not found";
                resolution = (resolution != null) ? resolution : "info not found";

                File file = new File(filePath);
                String folderPath = file.getParent();
                if (folderSet.add(folderPath)) {
                    folderInfoMap.put(folderPath, new GalleryVideoFolderInformation23rd(fileSize, title, duration, bucketDisplayName, dateAdded, resolution, 1));
                } else {
                    GalleryVideoFolderInformation23rd folderInfo = folderInfoMap.get(folderPath);
                    if (folderInfo != null) {
                        folderInfo.folderTotalSize += fileSize;
                    }
                    if (folderInfo != null) {
                        folderInfo.folderVideoCount++;
                    }
                }
            }
            cursor.close();
        }
        for (Map.Entry<String, GalleryVideoFolderInformation23rd> entry : folderInfoMap.entrySet()) {
            videoFolders.add(new GalleryVideoFolderDetails23rd(entry.getKey(), entry.getValue()));
        }

        return videoFolders;
    }

    public static ArrayList<GalleryVideoInformation23rd> getGalleryVideosFromFolder(Context context, String folderPath) {
        ArrayList<GalleryVideoInformation23rd> videoList = new ArrayList<>();

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.RESOLUTION
        };

        Log.e("TAG", "getGalleryVideosFromFolder: " + folderPath);
        String selection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{folderPath + "%"};
        String sortOrder = MediaStore.Video.Media.TITLE + " DESC";
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String bucketName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                long dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                String resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));

                // Set empty string if any value is null
                data = (data != null) ? data : "info not found";
                title = (title != null) ? title : "info not found";
                bucketName = (bucketName != null) ? bucketName : "info not found";
                resolution = (resolution != null) ? resolution : "info not found";

                GalleryVideoInformation23rd videoItem = new GalleryVideoInformation23rd(id, data, title, size, duration, bucketName, dateAdded, resolution);
                videoList.add(videoItem);
            }
            cursor.close();
        }

        return videoList;
    }

    public static ArrayList<GalleryVideoInformation23rd> getGalleryAllVideos(Context context) {
        ArrayList<GalleryVideoInformation23rd> videoList = new ArrayList<>();

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.RESOLUTION
        };
        String sortOrder = MediaStore.Video.Media.TITLE + " DESC";
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String bucketName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                long dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                String resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));


                // Set empty string if any value is null
                data = (data != null) ? data : "info not found";
                title = (title != null) ? title : "info not found";
                bucketName = (bucketName != null) ? bucketName : "info not found";
                resolution = (resolution != null) ? resolution : "info not found";

                GalleryVideoInformation23rd videoItem = new GalleryVideoInformation23rd(id, data, title, size, duration, bucketName, dateAdded, resolution);
                videoList.add(videoItem);
            }
            cursor.close();
        }

        return videoList;
    }

    public static ArrayList<MusicFile23rd> getGalleryMusicFiles(ContentResolver contentResolver) {
        ArrayList<MusicFile23rd> audioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE
        };
        String sortOrder = MediaStore.Audio.Media.TITLE + " DESC";
        Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder);

        if (cursor != null) {
            int idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            int durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                String title = cursor.getString(titleIndex);
                String album = cursor.getString(albumIndex);
                String artist = cursor.getString(artistIndex);
                String data = cursor.getString(dataIndex);
                long duration = cursor.getLong(durationIndex);
                long size = cursor.getLong(sizeIndex);

                // Set default values for any null fields
                title = (title != null) ? title : "info not found";
                album = (album != null) ? album : "info not found";
                artist = (artist != null) ? artist : "info not found";
                data = (data != null) ? data : "info not found";

                audioList.add(new MusicFile23rd(id, title, album, artist, data, duration, size));
            }
            cursor.close();
        }

        return audioList;
    }

    public static ProgressDialog progressDialog;

    public static void showLoader(Activity activity) {
        // Check if the activity is not null and not finishing
        if (activity != null && !activity.isFinishing()) {
            // Initialize and show the progress dialog only if it's not already showing
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setTitle("Loading...");
                progressDialog.setCancelable(false); // Optional: to prevent dialog from being dismissed on back button press
                progressDialog.show();
            }
        }
    }

    public static void dismissLoader() {
        // Check if the progress dialog is not null and is currently showing
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null; // Clear the reference to the progress dialog
        }
    }


    public static void shareVideo(Activity activity, String videoUrl) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            Uri uriForFile = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", new File(videoUrl));
            String mimeType = activity.getContentResolver().getType(uriForFile);
            intent.setDataAndType(uriForFile, mimeType);
            intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "videos");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(Intent.createChooser(intent, "Choose from the list to continue.."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareAudio(Activity activity, String videoUrl) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            Uri uriForFile = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", new File(videoUrl));
            String mimeType = activity.getContentResolver().getType(uriForFile);
            intent.setDataAndType(uriForFile, mimeType);
            intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "audios");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(Intent.createChooser(intent, "Choose from the list to continue.."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri getUriForVideo(Activity activity, Uri imageUri) {
        String[] projection = {MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{imageUri.getPath()};

        try (Cursor cursor = activity.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri getUriForAudio(Activity activity, Uri imageUri) {
        String[] projection = {MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{imageUri.getPath()};

        try (Cursor cursor = activity.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteVideo(Context context, Uri mediaUri) {
        ContentResolver contentResolver = context.getContentResolver();
        int rowsDeleted = 0;
        String mediaType = contentResolver.getType(mediaUri);

        if (mediaType != null) {
            String selection = MediaStore.Video.Media._ID + " = ?";
            String[] selectionArgs;

            String mediaId = mediaUri.getLastPathSegment();
            selectionArgs = new String[]{mediaId};

            Uri mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            try {
                rowsDeleted = contentResolver.delete(mediaContentUri, selection, selectionArgs);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            // Log.e("DeleteMedia", "Unknown media type for URI: " + mediaUri);
            return false;
        }

        return rowsDeleted > 0;
    }

    public static boolean deleteAudio(Context context, Uri mediaUri) {
        ContentResolver contentResolver = context.getContentResolver();
        int rowsDeleted = 0;
        String mediaType = contentResolver.getType(mediaUri);

        if (mediaType != null) {
            String selection = MediaStore.Audio.Media._ID + " = ?";
            String[] selectionArgs;

            String mediaId = mediaUri.getLastPathSegment();
            selectionArgs = new String[]{mediaId};

            Uri mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            try {
                rowsDeleted = contentResolver.delete(mediaContentUri, selection, selectionArgs);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            // Log.e("DeleteMedia", "Unknown media type for URI: " + mediaUri);
            return false;
        }

        return rowsDeleted > 0;
    }

    public static void shareApp(Activity activity) {
        String appPackageName = activity.getPackageName();
        String appName = activity.getString(R.string.app_name);
        String shareBody = appName + "https://play.google.com/store/apps/details?id=" + appPackageName;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "App Recommendation");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        activity.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    public static void launchMarket(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public static ArrayList<GalleryVideoInformation23rd> getVideoDetails(Context context) {
        ArrayList<GalleryVideoInformation23rd> videoDetailsList = new ArrayList<>();

        // File path to the "TrendingVideo" folder
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), File.separator + "TrendingVideo");
        String folderPath = folder.getAbsolutePath();

        // Define the projection (columns to retrieve)
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.RESOLUTION
        };

        // Query only videos from the specified folder
        String selection = MediaStore.Video.Media.DATA + " LIKE ?";
        String[] selectionArgs = new String[]{folderPath + "%"};

        // Perform the query
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String bucketName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                long dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                String resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));

                // Add to the list
                videoDetailsList.add(new GalleryVideoInformation23rd(idIndex, data, title, size, duration, bucketName, dateAdded, resolution));
            }
            cursor.close();
        } else {
            Log.e("VideoFetcher", "Cursor is null or no videos found!");
        }

        return videoDetailsList;
    }

    public static void downloadVideo(Context activity, String videoUrl, String folderName, String fileName) {
        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoUrl));
        request.setTitle("Downloading Video");
        request.setDescription("Please wait...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), File.separator + folderName);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Log.e("TAG", "Failed to create directory: " + folder.getAbsolutePath());
            }
        }
        request.setDestinationUri(Uri.fromFile(new File(folder, fileName)));
        Toast.makeText(activity, "Download Started", Toast.LENGTH_SHORT).show();

        if (downloadManager != null) {
            long downloadId = downloadManager.enqueue(request);
        }
    }

}
