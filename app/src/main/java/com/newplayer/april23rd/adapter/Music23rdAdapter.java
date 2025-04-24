package com.newplayer.april23rd.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.activity.PlayMusic23rdActivity;
import com.newplayer.april23rd.model.MusicFile23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Music23rdAdapter extends RecyclerView.Adapter<Music23rdAdapter.ViewHolder> {

    Activity activity;
    ArrayList<MusicFile23rd> musicFile23rdArrayList;
    private Uri deleteVideoUri;
    public int selectedPosition = 0;
    public static final int DELETE_REQUEST_CODE = 13;
    AdController23rd adController23rd;
    public static final int LAYOUT_VERTICAL = 0;
    public static final int LAYOUT_GRID = 1;
    private int layoutType = LAYOUT_VERTICAL;

    public Music23rdAdapter(Activity activity, ArrayList<MusicFile23rd> musicFile23rdArrayList) {
        this.activity = activity;
        this.musicFile23rdArrayList = musicFile23rdArrayList;
        adController23rd = AdController23rd.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (layoutType == LAYOUT_GRID) {
            view = LayoutInflater.from(activity).inflate(R.layout.audio_grid_layout_23rd, parent, false);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.audio_list_layout_23rd, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicFile23rd galleryAudioFile = musicFile23rdArrayList.get(position);
        holder.tvAudioName.setText(galleryAudioFile.getTitle());

        holder.tvSize.setText(formatFileSize(galleryAudioFile.getSize()));
        holder.tvDuration.setText(formatDuration(galleryAudioFile.getDuration()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PlayMusic23rdActivity.class);
            intent.putExtra("videoPath", musicFile23rdArrayList.get(position).getPath());
            activity.startActivity(intent);
        });

        holder.ivMore.setOnClickListener(v -> {
            showMoreAlertDialog(position);
        });
    }


    public void showMoreAlertDialog(int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(R.layout.more_info_bottom_sheet_23rd);

        TextView tvTitle = bottomSheetDialog.findViewById(R.id.tvTitle);
        TextView tvPlayVideo = bottomSheetDialog.findViewById(R.id.tvPlayVideo);
        TextView tvShareVideo = bottomSheetDialog.findViewById(R.id.tvShareVideo);
        TextView tvDeleteVideo = bottomSheetDialog.findViewById(R.id.tvDeleteVideo);
        TextView tvVideoInfo = bottomSheetDialog.findViewById(R.id.tvVideoInfo);
        LinearLayout llPlay = bottomSheetDialog.findViewById(R.id.llPlay);
        LinearLayout llShare = bottomSheetDialog.findViewById(R.id.llShare);
        LinearLayout llDelete = bottomSheetDialog.findViewById(R.id.llDelete);
        LinearLayout llDetails = bottomSheetDialog.findViewById(R.id.llDetails);
        ImageView ivClose = bottomSheetDialog.findViewById(R.id.ivClose);

        tvPlayVideo.setText("Play Audio");
        tvShareVideo.setText("Share Audio");
        tvDeleteVideo.setText("Delete Audio");
        tvVideoInfo.setText("Audio Info");

        llPlay.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PlayMusic23rdActivity.class);
            intent.putExtra("videoPath", musicFile23rdArrayList.get(position).getPath());
            activity.startActivity(intent);
            bottomSheetDialog.dismiss();
        });
        llShare.setOnClickListener(v -> {
            FetchStorageVideo23rd.shareVideo(activity, musicFile23rdArrayList.get(position).getPath());
            bottomSheetDialog.dismiss();
        });
        llDelete.setOnClickListener(v -> {
            deleteVideoUri = FetchStorageVideo23rd.getUriForAudio(activity, Uri.parse(musicFile23rdArrayList.get(position).getPath()));
            Uri uri = getAudioUri(Uri.parse(musicFile23rdArrayList.get(position).getPath()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                selectedPosition = position;
                try {
                    deleteAboveVideos(uri);
                } catch (IntentSender.SendIntentException e1) {
                    e1.printStackTrace();
                }
            } else {
                boolean deleted = FetchStorageVideo23rd.deleteAudio(activity, deleteVideoUri);
                if (deleted) {
                    Toast.makeText(activity, "Audio Deleted successfully", Toast.LENGTH_SHORT).show();
                    musicFile23rdArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "Audio Deleted failed..try again...", Toast.LENGTH_SHORT).show();
                }
            }
            bottomSheetDialog.dismiss();
        });
        llDetails.setOnClickListener(v -> {
            videoInformationBottomSheet(position);
            bottomSheetDialog.dismiss();
        });
        tvTitle.setText(getFileNameWithExtension(musicFile23rdArrayList.get(position).getPath()));
        ivClose.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    public void videoInformationBottomSheet(int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(R.layout.audio_info_bottom_sheet_23rd);

        TextView tvAudioName = bottomSheetDialog.findViewById(R.id.tvAudioName);
        TextView tvAlbumName = bottomSheetDialog.findViewById(R.id.tvAlbumName);
        TextView tvArtist = bottomSheetDialog.findViewById(R.id.tvArtist);
        TextView tvFormat = bottomSheetDialog.findViewById(R.id.tvFormat);
        TextView tvSize = bottomSheetDialog.findViewById(R.id.tvSize);
        TextView tvDuration = bottomSheetDialog.findViewById(R.id.tvDuration);


        tvAudioName.setText(musicFile23rdArrayList.get(position).getTitle());
        tvAlbumName.setText(musicFile23rdArrayList.get(position).getAlbum());
        tvArtist.setText(musicFile23rdArrayList.get(position).getArtist());
        tvFormat.setText(getVideoFileExtension(musicFile23rdArrayList.get(position).getPath()));
        tvSize.setText(formatFileSize(musicFile23rdArrayList.get(position).getSize()));
        tvDuration.setText(formatDuration(musicFile23rdArrayList.get(position).getDuration()));

        bottomSheetDialog.show();
    }

    public String formatVideoFileSize(long size) {
        return Formatter.formatFileSize(activity, size);
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

    public static String convertTimestampToDate(long timestamp) {
        if (timestamp < 1000000000000L) {
            timestamp *= 1000;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Date date = new Date(timestamp);
        return dateFormat.format(date);
    }

    public static String getFileNameWithExtension(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    @Override
    public int getItemViewType(int position) {
        return layoutType;
    }


    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
        notifyDataSetChanged();
    }

    public String formatFileSize(long size) {
        return Formatter.formatFileSize(activity, size);
    }


    @Override
    public int getItemCount() {
        return musicFile23rdArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAudioName, tvSize, tvDuration;
        ImageView ivMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAudioName = itemView.findViewById(R.id.tvAudioName);
            tvSize = itemView.findViewById(R.id.tvSize);
            ivMore = itemView.findViewById(R.id.ivMore);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }
    }

    public int getPos() {
        musicFile23rdArrayList.remove(selectedPosition);
        return this.selectedPosition;
    }

    public Uri getAudioUri(Uri imageUri) {
        String[] projections = {MediaStore.MediaColumns._ID};
        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
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
        return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf((int) id));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void deleteAboveVideos(Uri imageUri) throws IntentSender.SendIntentException {
        ContentResolver contentResolver = activity.getContentResolver();

        List<Uri> uriList = new ArrayList<>();
        Collections.addAll(uriList, imageUri);
        PendingIntent pendingIntent = MediaStore.createDeleteRequest(contentResolver, uriList);
        ((Activity) activity).startIntentSenderForResult(pendingIntent.getIntentSender(),
                DELETE_REQUEST_CODE, null, 0,
                0, 0, null);

    }


    public static String getVideoFileExtension(String filePath) {
        if (filePath.equals("info not found")) {
            return filePath;
        } else {
            int lastDotIndex = filePath.lastIndexOf('.');
            if (lastDotIndex != -1 && lastDotIndex < filePath.length() - 1) {
                return filePath.substring(lastDotIndex);
            } else {
                return "";
            }
        }
    }

    private String formatDuration(long duration) {
        long seconds = duration / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, remainingSeconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds);
        }
    }

}
