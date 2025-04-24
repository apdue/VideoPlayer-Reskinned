package com.newplayer.april23rd.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.activity.PlayVideo23rdActivity;
import com.newplayer.april23rd.model.GalleryVideoInformation23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DownloadVideo23rdAdapter extends RecyclerView.Adapter<DownloadVideo23rdAdapter.ViewHolder> {

    Activity activity;
    ArrayList<GalleryVideoInformation23rd> galleryVideoInformation23rdArrayList;
    public static final int LAYOUT_VERTICAL = 0;
    public static final int LAYOUT_GRID = 1;
    private int layoutType = LAYOUT_VERTICAL;
    private Uri deleteVideoUri;
    public int selectedPosition = 0;
    public static final int DELETE_REQUEST_CODE = 13;
    AdController23rd adsController;

    public DownloadVideo23rdAdapter(Activity activity, ArrayList<GalleryVideoInformation23rd> galleryVideoInformation23rdArrayList) {
        this.activity = activity;
        this.galleryVideoInformation23rdArrayList = galleryVideoInformation23rdArrayList;
        adsController = AdController23rd.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.video_list_layout_23rd, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GalleryVideoInformation23rd videoItem = galleryVideoInformation23rdArrayList.get(position);
        holder.tvTitle.setText(getFileNameWithExtension(galleryVideoInformation23rdArrayList.get(position).getData()));
        holder.tvVideoDuration.setText(formatDuration(videoItem.getDuration()));
        holder.tvSize.setText(formatVideoFileSize(videoItem.getSize()));

        Glide.with(activity)
                .asBitmap()
                .load(getMediaUri(videoItem.getData()))
                .placeholder(R.drawable.ic_logo_23rd)
                .error(R.drawable.ic_logo_23rd)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);

        holder.ivMore.setOnClickListener(v -> {
            moreBottomSheet(position);
        });

        holder.itemView.setOnClickListener(v -> {
            adsController.showInterAdCallBack(activity, new AdController23rd.MyCallback() {
                @Override
                public void callbackCall() {
                    Intent intent = new Intent(activity, PlayVideo23rdActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("video_title", galleryVideoInformation23rdArrayList.get(position).getTitle());
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("videoArrayList", galleryVideoInformation23rdArrayList);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
        });
    }

    private Uri getMediaUri(String filePath) {
        Uri fileUri = null;
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{filePath};

        // Query the MediaStore for the file URI
        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,  // Change this for images/audio
                new String[]{MediaStore.MediaColumns._ID},
                selection,
                selectionArgs,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            fileUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            cursor.close();
        }

        return fileUri;
    }

    @Override
    public int getItemViewType(int position) {
        return layoutType;
    }


    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return galleryVideoInformation23rdArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvVideoDuration;
        TextView tvSize;
        ImageView ivMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvVideoDuration = itemView.findViewById(R.id.tvVideoDuration);
            tvSize = itemView.findViewById(R.id.tvSize);
            ivMore = itemView.findViewById(R.id.ivMore);
        }
    }

    private String formatDuration(long duration) {
        long seconds = duration / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        if (hours > 0) {
            // Include hours in the format if duration includes hours
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, remainingSeconds);
        } else {
            // Only show minutes and seconds otherwise
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds);
        }
    }

    public String formatVideoFileSize(long size) {
        return Formatter.formatFileSize(activity, size);
    }

    public static String convertTimestampToDate(long timestamp) {
        if (timestamp < 1000000000000L) {
            timestamp *= 1000;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Date date = new Date(timestamp);
        return dateFormat.format(date);
    }

    public static String getFileExtension(String filePath) {
        String extension = "";
        String mimeType = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (mimeType != null && !mimeType.isEmpty()) {
            extension = mimeType;
        } else {
            int dotIndex = filePath.lastIndexOf(".");
            if (dotIndex != -1) {
                extension = filePath.substring(dotIndex + 1);
            }
        }

        return extension;
    }

    public static String getFileNameWithExtension(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    private void moreBottomSheet(int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(R.layout.more_info_bottom_sheet_23rd);


        ImageView ivClose = bottomSheetDialog.findViewById(R.id.ivClose);
        LinearLayout llPlay = bottomSheetDialog.findViewById(R.id.llPlay);
        LinearLayout llShare = bottomSheetDialog.findViewById(R.id.llShare);
        LinearLayout llDelete = bottomSheetDialog.findViewById(R.id.llDelete);
        LinearLayout llDetails = bottomSheetDialog.findViewById(R.id.llDetails);

        ivClose.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        llPlay.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PlayVideo23rdActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("video_title", galleryVideoInformation23rdArrayList.get(position).getTitle());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("videoArrayList", galleryVideoInformation23rdArrayList);
            intent.putExtras(bundle);
            activity.startActivity(intent);
            bottomSheetDialog.dismiss();
        });
        llShare.setOnClickListener(v -> {
            FetchStorageVideo23rd.shareVideo(activity, galleryVideoInformation23rdArrayList.get(position).getData());
            bottomSheetDialog.dismiss();
        });
        llDelete.setOnClickListener(v -> {
            deleteVideoUri = FetchStorageVideo23rd.getUriForVideo(activity, Uri.parse(galleryVideoInformation23rdArrayList.get(position).getData()));
            Uri uri = getVideoUri(Uri.parse(galleryVideoInformation23rdArrayList.get(position).getData()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                selectedPosition = position;
                try {
                    deleteAboveVideos(uri);
                } catch (IntentSender.SendIntentException e1) {
                    e1.printStackTrace();
                }
            } else {
                boolean deleted = FetchStorageVideo23rd.deleteVideo(activity, deleteVideoUri);
                if (deleted) {
                    Toast.makeText(activity, "Video Deleted successfully", Toast.LENGTH_SHORT).show();
                    galleryVideoInformation23rdArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "Video Deleted failed..try again...", Toast.LENGTH_SHORT).show();
                }
            }
            bottomSheetDialog.dismiss();
        });
        llDetails.setOnClickListener(v -> {
            videoInformationBottomSheet(position);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
    

    public void videoInformationBottomSheet(int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(R.layout.video_info_bottom_sheet_23rd);


        TextView tvFileName = bottomSheetDialog.findViewById(R.id.tvFileName);
        TextView tvLocation = bottomSheetDialog.findViewById(R.id.tvLocation);
        TextView tvSize = bottomSheetDialog.findViewById(R.id.tvSize);
        TextView tvDate = bottomSheetDialog.findViewById(R.id.tvDate);
        TextView tvFormat = bottomSheetDialog.findViewById(R.id.tvFormat);
        TextView tvResolution = bottomSheetDialog.findViewById(R.id.tvResolution);
        TextView tvDuration = bottomSheetDialog.findViewById(R.id.tvDuration);


        tvFileName.setText(getFileNameWithExtension(galleryVideoInformation23rdArrayList.get(position).getData()));
        tvLocation.setText(galleryVideoInformation23rdArrayList.get(position).getData());
        tvSize.setText(formatVideoFileSize(galleryVideoInformation23rdArrayList.get(position).getSize()));
        tvDate.setText(convertTimestampToDate(galleryVideoInformation23rdArrayList.get(position).getDateAdded()));
        tvFormat.setText(getFileExtension(galleryVideoInformation23rdArrayList.get(position).getData()));
        tvResolution.setText(galleryVideoInformation23rdArrayList.get(position).getResolution());
        tvDuration.setText(formatDuration(galleryVideoInformation23rdArrayList.get(position).getDuration()));

        bottomSheetDialog.show();
    }

    public Uri getVideoUri(Uri imageUri) {
        String[] projections = {MediaStore.MediaColumns._ID};
        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projections,
                MediaStore.MediaColumns.DATA + "=?",
                new String[]{imageUri.getPath()}, null);
        long id = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf((int) id));
    }

    public int getPos() {
        galleryVideoInformation23rdArrayList.remove(selectedPosition);
        return this.selectedPosition;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void deleteAboveVideos(Uri imageUri) throws IntentSender.SendIntentException {
        ContentResolver contentResolver = activity.getContentResolver();

        List<Uri> uriList = new ArrayList<>();
        Collections.addAll(uriList, imageUri);
        PendingIntent pendingIntent = MediaStore.createDeleteRequest(contentResolver, uriList);
        activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
                DELETE_REQUEST_CODE, null, 0,
                0, 0, null);

    }
}
