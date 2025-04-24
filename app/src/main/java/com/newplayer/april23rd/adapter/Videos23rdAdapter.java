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

public class Videos23rdAdapter extends RecyclerView.Adapter<Videos23rdAdapter.ViewHolder> {

    ArrayList<GalleryVideoInformation23rd> videoInformationArrayList;
    Activity activity;
    public static final int LAYOUT_VERTICAL = 0;
    public static final int LAYOUT_GRID = 1;
    private int layoutType = LAYOUT_VERTICAL;

    private Uri deleteVideoUri;
    public int selectedPosition = 0;
    public static final int DELETE_REQUEST_CODE = 13;
    AdController23rd adController23rd;

    public Videos23rdAdapter(ArrayList<GalleryVideoInformation23rd> videoInformationArrayList, Activity activity) {
        this.videoInformationArrayList = videoInformationArrayList;
        this.activity = activity;
        adController23rd = AdController23rd.getInstance();
    }

    @NonNull
    @Override
    public Videos23rdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (layoutType == LAYOUT_GRID) {
            view = LayoutInflater.from(activity).inflate(R.layout.video_grid_layout_23rd, parent, false);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.video_list_layout_23rd, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Videos23rdAdapter.ViewHolder holder, int position) {
        GalleryVideoInformation23rd galleryVideoInformation23rd = videoInformationArrayList.get(position);

        Glide.with(activity)
                .asBitmap()
                .load(getMediaUri(galleryVideoInformation23rd.getData()))
                .placeholder(R.drawable.ic_logo_23rd)
                .error(R.drawable.ic_logo_23rd)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);

        holder.tvTitle.setSelected(true);
        holder.tvTitle.setText(getFileNameWithExtension(galleryVideoInformation23rd.getData()));
        holder.tvVideoDuration.setText(formatDuration(galleryVideoInformation23rd.getDuration()));
        holder.tvSize.setText(formatVideoFileSize(galleryVideoInformation23rd.getSize()));

        holder.ivMore.setOnClickListener(v -> {
            moreBottomSheet(position);
        });

        holder.itemView.setOnClickListener(v -> {
            adController23rd.showInterAdCallBack(activity, new AdController23rd.MyCallback() {
                @Override
                public void callbackCall() {
                    Intent intent = new Intent(activity, PlayVideo23rdActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("video_title", videoInformationArrayList.get(position).getTitle());
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("videoArrayList", videoInformationArrayList);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
        });

    }

    public static String getFileNameWithExtension(String filePath) {
        File file = new File(filePath);
        return file.getName();
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
        return videoInformationArrayList.size();
    }

    private Uri getMediaUri(String filePath) {
        Uri fileUri = null;
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{filePath};

        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon, ivMore;
        TextView tvVideoDuration, tvTitle, tvSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvVideoDuration = itemView.findViewById(R.id.tvVideoDuration);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSize = itemView.findViewById(R.id.tvSize);
            ivMore = itemView.findViewById(R.id.ivMore);
        }
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
            intent.putExtra("video_title", videoInformationArrayList.get(position).getTitle());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("videoArrayList", videoInformationArrayList);
            intent.putExtras(bundle);
            activity.startActivity(intent);
            bottomSheetDialog.dismiss();
        });
        llShare.setOnClickListener(v -> {
            FetchStorageVideo23rd.shareVideo(activity, videoInformationArrayList.get(position).getData());
            bottomSheetDialog.dismiss();
        });
        llDelete.setOnClickListener(v -> {
            deleteVideoUri = FetchStorageVideo23rd.getUriForVideo(activity, Uri.parse(videoInformationArrayList.get(position).getData()));
            Uri uri = getVideoUri(Uri.parse(videoInformationArrayList.get(position).getData()));
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
                    videoInformationArrayList.remove(position);
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
        videoInformationArrayList.remove(selectedPosition);
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


        tvFileName.setText(getFileNameWithExtension(videoInformationArrayList.get(position).getData()));
        tvLocation.setText(videoInformationArrayList.get(position).getData());
        tvSize.setText(formatVideoFileSize(videoInformationArrayList.get(position).getSize()));
        tvDate.setText(convertTimestampToDate(videoInformationArrayList.get(position).getDateAdded()));
        tvFormat.setText(getFileExtension(videoInformationArrayList.get(position).getData()));
        tvResolution.setText(videoInformationArrayList.get(position).getResolution());
        tvDuration.setText(formatDuration(videoInformationArrayList.get(position).getDuration()));

        bottomSheetDialog.show();
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
}
