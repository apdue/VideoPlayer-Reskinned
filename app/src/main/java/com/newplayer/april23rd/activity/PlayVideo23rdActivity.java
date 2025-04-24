package com.newplayer.april23rd.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bullhead.equalizer.EqualizerFragment;
import com.bullhead.equalizer.Settings;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.VideoPlaybackIcons23rdAdapter;
import com.newplayer.april23rd.model.GalleryVideoInformation23rd;
import com.newplayer.april23rd.model.VideoPlaybackIconModel23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;
import com.newplayer.april23rd.utils.GalleryVideoBrightnessDialog23rd;
import com.newplayer.april23rd.utils.GalleryVideoVolumeDialog23rd;
import com.newplayer.april23rd.utils.OnSwipeTouchListener23rd;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class PlayVideo23rdActivity extends AppCompatActivity implements View.OnClickListener{

    private int device_height, device_width, brightness, media_volume;

    com.google.android.exoplayer2.ui.PlayerView playerView;
    SimpleExoPlayer player;
    boolean start = false;
    boolean left, right;
    private float baseX, baseY;
    boolean swipe_move = false;
    private long diffX, diffY;
    public static final int MINIMUM_DISTANCE = 100;
    private ContentResolver contentResolver;
    private Window window;
    boolean singleTap = false;
    LinearLayout volumeProgressContainer, volumeTextContainer, brightnessProgressContainer, brightnessTextContainer;
    ImageView volumeIcon, brightnessIcon;
    AudioManager audioManager;
    TextView volumeTextView, brightnessTextView, durationTextView;
    ProgressBar volumeProgress, brightnessProgress;
    boolean success = false;
    ScaleGestureDetector scaleGestureDetector;
    private float scale_factor = 1.0f;
    boolean double_tap = false;
    RelativeLayout double_tap_playpause;
    RelativeLayout zoomLayout;
    RelativeLayout zoomContainer;
    TextView zoom_perc;
    private ArrayList<VideoPlaybackIconModel23rd> iconModelArrayList = new ArrayList<>();
    VideoPlaybackIcons23rdAdapter playbackIconsAdapter;
    RecyclerView videoIconRecyclerView;
    boolean expand = false;
    View nightModeView;
    boolean dark = false;
    boolean mute = false;
    PictureInPictureParams.Builder pictureInPicture;
    boolean isCrossChecked;
    FrameLayout eqFrameLayout;
    float speed;
    PlaybackParameters parameters;
    DialogProperties dialogProperties;
    FilePickerDialog filePickerDialog;
    Uri uriSubtitle;
    ConcatenatingMediaSource concatenatingMediaSource;
    ImageView nextImageView, prevImageView;

    int position;
    String videoTitle;
    TextView videoTitleTextView;
    ArrayList<GalleryVideoInformation23rd> mVideoFiles = new ArrayList<>();
    private ControlsMode controlsMode;

    public enum ControlsMode {
        LOCK, FULLSCREEN
    }


    private PlayerNotificationManager playerNotificationManager;

    private static final String CHANNEL_ID = "playback_channel";
    private static final int NOTIFICATION_ID = 1;

    ImageView lockImageView, unlockImageView, scalingImageView, videoShareImageView, backImageView;
    RelativeLayout mainRelativeLayout;
    private static final int FILE_PICKER_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_23rd);

        hideBottomBar();
//        videoPath = getIntent().getStringExtra("videoPath");

        position = getIntent().getIntExtra("position", 1);
        videoTitle = getIntent().getStringExtra("video_title");
        mVideoFiles = getIntent().getExtras().getParcelableArrayList("videoArrayList");
        nextImageView = findViewById(R.id.nextImageView);
        prevImageView = findViewById(R.id.prevImageView);
        durationTextView = findViewById(R.id.durationTextView);
        videoTitleTextView = findViewById(R.id.videoTitleTextView);
        lockImageView = findViewById(R.id.lockImageView);
        unlockImageView = findViewById(R.id.unlockImageView);
        scalingImageView = findViewById(R.id.scalingImageView);
        mainRelativeLayout = findViewById(R.id.mainRelativeLayout);
        nightModeView = findViewById(R.id.nightModeView);
        videoShareImageView = findViewById(R.id.videoShareImageView);
        backImageView = findViewById(R.id.backImageView);
        videoIconRecyclerView = findViewById(R.id.videoIconRecyclerView);
        eqFrameLayout = findViewById(R.id.eqFrameLayout);
        volumeTextView = findViewById(R.id.volumeTextView);
        brightnessTextView = findViewById(R.id.brightnessTextView);
        volumeProgress = findViewById(R.id.volumeProgress);
        brightnessProgress = findViewById(R.id.brightnessProgress);
        volumeProgressContainer = findViewById(R.id.volumeProgressContainer);
        brightnessProgressContainer = findViewById(R.id.brightnessProgressContainer);
        volumeTextContainer = findViewById(R.id.volumeTextContainer);
        brightnessTextContainer = findViewById(R.id.brightnessTextContainer);
        volumeIcon = findViewById(R.id.volumeIcon);
        brightnessIcon = findViewById(R.id.brightnessIcon);
        zoomLayout = findViewById(R.id.zoom_layout);
        zoom_perc = findViewById(R.id.zoom_percentage);
        zoomContainer = findViewById(R.id.zoom_container);
        double_tap_playpause = findViewById(R.id.double_tap_play_pause);
        playerView = findViewById(R.id.exoplayer_view);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleDetector());

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        videoTitleTextView.setText(videoTitle);
        nextImageView.setOnClickListener(this);
        prevImageView.setOnClickListener(this);
        lockImageView.setOnClickListener(this);
        unlockImageView.setOnClickListener(this);
        videoShareImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        scalingImageView.setOnClickListener(firstListener);

        double milliseconds = Double.parseDouble(String.valueOf(mVideoFiles.get(position).getDuration()));
        durationTextView.setText(timeConversion((long) milliseconds));

        dialogProperties = new DialogProperties();
        filePickerDialog = new FilePickerDialog(PlayVideo23rdActivity.this);
        filePickerDialog.setTitle("Select a Subtitle File");
        filePickerDialog.setPositiveBtnName("OK");
        filePickerDialog.setNegativeBtnName("Cancel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPicture = new PictureInPictureParams.Builder();
        }

        playVideo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPicture = new PictureInPictureParams.Builder();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        device_width = displayMetrics.widthPixels;
        device_height = displayMetrics.heightPixels;


        playerView.setOnTouchListener(new OnSwipeTouchListener23rd(this) {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        playerView.showController();
                        start = true;
                        if (motionEvent.getX() < (device_width / 2)) {
                            left = true;
                            right = false;
                        } else if (motionEvent.getX() > (device_width / 2)) {
                            left = false;
                            right = true;
                        }
                        baseX = motionEvent.getX();
                        baseY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        swipe_move = true;
                        diffX = (long) Math.ceil(motionEvent.getX() - baseX);
                        diffY = (long) Math.ceil(motionEvent.getY() - baseY);
                        double brightnessSpeed = 0.01;
                        if (Math.abs(diffY) > MINIMUM_DISTANCE) {
                            start = true;
                            if (Math.abs(diffY) > Math.abs(diffX)) {
                                boolean value;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    value = android.provider.Settings.System.canWrite(getApplicationContext());
                                    if (value) {
                                        if (left) {
                                            contentResolver = getContentResolver();
                                            window = getWindow();
                                            try {
                                                android.provider.Settings.System.putInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                                                brightness = android.provider.Settings.System.getInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS);
                                            } catch (
                                                    android.provider.Settings.SettingNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            int new_brightness = (int) (brightness - (diffY * brightnessSpeed));
                                            if (new_brightness > 250) {
                                                new_brightness = 250;
                                            } else if (new_brightness < 1) {
                                                new_brightness = 1;
                                            }
                                            double brt_percentage = Math.ceil((((double) new_brightness / (double) 250) * (double) 100));
                                            brightnessProgressContainer.setVisibility(View.VISIBLE);
                                            brightnessTextContainer.setVisibility(View.VISIBLE);
                                            brightnessProgress.setProgress((int) brt_percentage);

                                            if (brt_percentage < 30) {
                                                brightnessIcon.setImageResource(R.drawable.ic_brightness_low_23rd);
                                            } else if (brt_percentage > 30 && brt_percentage < 80) {
                                                brightnessIcon.setImageResource(R.drawable.ic_brightness_moderate_23rd);
                                            } else if (brt_percentage > 80) {
                                                brightnessIcon.setImageResource(R.drawable.ic_brightness_23rd);
                                            }

                                            brightnessTextView.setText(" " + (int) brt_percentage + "%");
                                            android.provider.Settings.System.putInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, (new_brightness));
                                            WindowManager.LayoutParams layoutParams = window.getAttributes();
                                            layoutParams.screenBrightness = brightness / (float) 255;
                                            window.setAttributes(layoutParams);
                                        } else if (right) {
                                            volumeTextContainer.setVisibility(View.VISIBLE);
                                            media_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                            int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                                            double cal = (double) diffY * ((double) maxVol / ((double) (device_height * 2) - brightnessSpeed));
                                            int newMediaVolume = media_volume - (int) cal;
                                            if (newMediaVolume > maxVol) {
                                                newMediaVolume = maxVol;
                                            } else if (newMediaVolume < 1) {
                                                newMediaVolume = 0;
                                            }
                                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newMediaVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                                            double volPer = Math.ceil((((double) newMediaVolume / (double) maxVol) * (double) 100));
                                            volumeTextView.setText(" " + (int) volPer + "%");
                                            if (volPer < 1) {
                                                volumeIcon.setImageResource(R.drawable.ic_volume_off_23rd);
                                                volumeTextView.setVisibility(View.VISIBLE);
                                                volumeTextView.setText("Off");
                                            } else if (volPer >= 1) {
                                                volumeIcon.setImageResource(R.drawable.ic_volume_23rd);
                                                volumeTextView.setVisibility(View.VISIBLE);
                                            }
                                            volumeProgressContainer.setVisibility(View.VISIBLE);
                                            volumeProgress.setProgress((int) volPer);
                                        }
                                        success = true;
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Allow write settings for swipe controls", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, 111);
                                    }
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        swipe_move = false;
                        start = false;
                        volumeProgressContainer.setVisibility(View.GONE);
                        brightnessProgressContainer.setVisibility(View.GONE);
                        volumeTextContainer.setVisibility(View.GONE);
                        brightnessTextContainer.setVisibility(View.GONE);
                        break;
                }
                scaleGestureDetector.onTouchEvent(motionEvent);
                return super.onTouch(view, motionEvent);
            }

            @Override
            public void onDoubleTouch() {
                if (double_tap) {
                    player.setPlayWhenReady(true);
                    double_tap_playpause.setVisibility(View.GONE);
                    double_tap = false;
                } else {
                    player.setPlayWhenReady(false);
                    double_tap_playpause.setVisibility(View.VISIBLE);
                    double_tap = true;
                }
            }

            @Override
            public void onSingleTouch() {
                if (singleTap) {
                    playerView.showController();
                    singleTap = false;
                } else {
                    playerView.hideController();
                    singleTap = true;
                }
                if (double_tap_playpause.getVisibility() == View.VISIBLE) {
                    double_tap_playpause.setVisibility(View.GONE);
                }
            }
        });

        horizontalIconList();

    }


    public static String timeConversion(Long millie) {
        if (millie != null) {
            long seconds = (millie / 1000);
            long sec = seconds % 60;
            long min = (seconds / 60) % 60;
            long hrs = (seconds / (60 * 60)) % 24;
            if (hrs > 0) {
                return String.format("%02d:%02d:%02d", hrs, min, sec);
            } else {
                return String.format("%02d:%02d", min, sec);
            }
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backImageView) {
            Log.e("TAG", "onClick: " + player);
            if (player != null) {
                player.release();
            }
            finish();
        }
        if (id == R.id.lockImageView) {
            controlsMode = ControlsMode.FULLSCREEN;
            mainRelativeLayout.setVisibility(View.VISIBLE);
            lockImageView.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "unLocked", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.unlockImageView) {
            controlsMode = ControlsMode.LOCK;
            mainRelativeLayout.setVisibility(View.INVISIBLE);
            lockImageView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Locked", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.nextImageView) {
            try {
                player.stop();
                position++;
                playVideo();
                videoTitleTextView.setText(mVideoFiles.get(position).getTitle());
            } catch (Exception e) {
                Toast.makeText(this, "no Next Video", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (id == R.id.prevImageView) {
            try {
                player.stop();
                position--;
                playVideo();
                videoTitleTextView.setText(mVideoFiles.get(position).getTitle());
            } catch (Exception e) {
                Toast.makeText(this, "no Previous Video", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (id == R.id.videoShareImageView) {
            FetchStorageVideo23rd.shareVideo(PlayVideo23rdActivity.this, mVideoFiles.get(position).getData());
        }

    }

    private class ScaleDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale_factor *= detector.getScaleFactor();
            scale_factor = Math.max(0.5f, Math.min(scale_factor, 6.0f));

            zoomLayout.setScaleX(scale_factor);
            zoomLayout.setScaleY(scale_factor);
            int percentage = (int) (scale_factor * 100);
            zoom_perc.setText(" " + percentage + "%");
            zoomContainer.setVisibility(View.VISIBLE);

            brightnessTextContainer.setVisibility(View.GONE);
            volumeTextContainer.setVisibility(View.GONE);
            brightnessProgressContainer.setVisibility(View.GONE);
            volumeProgressContainer.setVisibility(View.GONE);

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            zoomContainer.setVisibility(View.GONE);
            super.onScaleEnd(detector);
        }
    }

    private void horizontalIconList() {
        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_right_23rd, ""));
        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_night_mode_23rd, "Night"));
        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_pip_mode_23rd, "Popup"));
        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_equalizer_23rd, "Equalizer"));
        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_rotate_23rd, "Rotate"));

        playbackIconsAdapter = new VideoPlaybackIcons23rdAdapter(iconModelArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true);
        videoIconRecyclerView.setLayoutManager(layoutManager);
        videoIconRecyclerView.setAdapter(playbackIconsAdapter);
        playbackIconsAdapter.notifyDataSetChanged();
        playbackIconsAdapter.setOnItemClickListener(new VideoPlaybackIcons23rdAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    if (expand) {
                        iconModelArrayList.clear();
                        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_right_23rd, ""));
                        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_night_mode_23rd, "Night"));
                        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_pip_mode_23rd, "Popup"));
                        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_equalizer_23rd, "Equalizer"));
                        iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_rotate_23rd, "Rotate"));
                        playbackIconsAdapter.notifyDataSetChanged();
                        expand = false;
                    } else {
                        if (iconModelArrayList.size() == 5) {
                            iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_volume_off_23rd, "Mute"));
                            iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_volume_23rd, "Volume"));
                            iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_brightness_23rd, "Brightness"));
                            iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_speed_23rd, "Speed"));
                            iconModelArrayList.add(new VideoPlaybackIconModel23rd(R.drawable.ic_subtitle_23rd, "Subtitle"));
                        }
                        iconModelArrayList.set(position, new VideoPlaybackIconModel23rd(R.drawable.ic_left_23rd, ""));
                        playbackIconsAdapter.notifyDataSetChanged();
                        expand = true;
                    }
                }
                if (position == 1) {
                    //night mode
                    if (dark) {
                        nightModeView.setVisibility(View.GONE);
                        iconModelArrayList.set(position, new VideoPlaybackIconModel23rd(R.drawable.ic_night_mode_23rd, "Night"));
                        playbackIconsAdapter.notifyDataSetChanged();
                        dark = false;
                    } else {
                        nightModeView.setVisibility(View.VISIBLE);
                        iconModelArrayList.set(position, new VideoPlaybackIconModel23rd(R.drawable.ic_night_mode_23rd, "Day"));
                        playbackIconsAdapter.notifyDataSetChanged();
                        dark = true;
                    }
                }
                if (position == 2) {
                    //popup
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Rational aspectRatio = new Rational(16, 9);
                        pictureInPicture.setAspectRatio(aspectRatio);
                        enterPictureInPictureMode(pictureInPicture.build());
                    } else {
                        Log.wtf("not oreo", "yes");
                    }
                    //
                }
                if (position == 3) {
                    if (eqFrameLayout.getVisibility() == View.GONE) {
                        eqFrameLayout.setVisibility(View.VISIBLE);
                    }
                    final int sessionId = player.getAudioSessionId();
                    Settings.isEditing = false;
                    EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
                            .setAccentColor(Color.parseColor("#1A78F2"))
                            .setAudioSessionId(sessionId)
                            .build();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.eqFrameLayout, equalizerFragment)
                            .commit();
                    playbackIconsAdapter.notifyDataSetChanged();

                }
                if (position == 4) {
                    //rotate
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        playbackIconsAdapter.notifyDataSetChanged();
                    } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        playbackIconsAdapter.notifyDataSetChanged();
                    }


                }
                if (position == 5) {
                    //mute
                    if (mute) {
                        player.setVolume(100);
                        iconModelArrayList.set(position, new VideoPlaybackIconModel23rd(R.drawable.ic_volume_off_23rd, "Mute"));
                        playbackIconsAdapter.notifyDataSetChanged();
                        mute = false;
                    } else {
                        player.setVolume(0);
                        iconModelArrayList.set(position, new VideoPlaybackIconModel23rd(R.drawable.ic_volume_23rd, "unMute"));
                        playbackIconsAdapter.notifyDataSetChanged();
                        mute = true;
                    }


                }
                if (position == 6) {
                    //volume
                    GalleryVideoVolumeDialog23rd volumeDialog = new GalleryVideoVolumeDialog23rd();
                    volumeDialog.show(getSupportFragmentManager(), "dialog");
                    playbackIconsAdapter.notifyDataSetChanged();


                }
                if (position == 7) {
                    //brightness
                    GalleryVideoBrightnessDialog23rd brightnessDialog = new GalleryVideoBrightnessDialog23rd();
                    brightnessDialog.show(getSupportFragmentManager(), "dialog");
                    playbackIconsAdapter.notifyDataSetChanged();


                }
                if (position == 8) {
                    //speed
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayVideo23rdActivity.this);
                    alertDialog.setTitle("Select PLayback Speed").setPositiveButton("OK", null);
                    String[] items = {"0.5x", "1x Normal Speed", "1.25x", "1.5x", "2x"};
                    int checkedItem = -1;
                    alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    speed = 0.5f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 1:
                                    speed = 1f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 2:
                                    speed = 1.25f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 3:
                                    speed = 1.5f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 4:
                                    speed = 2f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.show();


                }
                if (position == 9) {
                    //subtitle
                    openFilePicker();
                }
            }
        });
    }

    private void openFilePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/x-subrip", "text/plain"});
            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
        } else {
            dialogProperties.selection_mode = DialogConfigs.SINGLE_MODE;
            dialogProperties.extensions = new String[]{".srt"};
            dialogProperties.root = new File("/storage/emulated/0");
            filePickerDialog.setProperties(dialogProperties);
            filePickerDialog.show();
            filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
                @Override
                public void onSelectedFilePaths(String[] files) {
                    for (String path : files) {
                        File file = new File(path);
                        uriSubtitle = Uri.parse(file.getAbsolutePath().toString());
                    }
                    playVideoSubtitle(uriSubtitle);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uriSubtitle = data.getData();
                if (uriSubtitle != null) {
                    playVideoSubtitle(uriSubtitle);
                }
            }
        }
    }

    private void playVideo() {
        String path = mVideoFiles.get(position).getData();
        Uri uri = Uri.parse(path);
        player = new SimpleExoPlayer.Builder(this).build();

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                this, Util.getUserAgent(this, "app"));

        concatenatingMediaSource = new ConcatenatingMediaSource();

        for (int i = 0; i < mVideoFiles.size(); i++) {
            String videoPath = mVideoFiles.get(i).getData();
            Uri videoUri = getMediaUri(videoPath);

            MediaItem mediaItem = MediaItem.fromUri(videoUri);

            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem);

            concatenatingMediaSource.addMediaSource(mediaSource);
        }

        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.setPlaybackParameters(parameters);

        player.setMediaSource(concatenatingMediaSource);
        player.prepare();
        player.seekTo(position, C.TIME_UNSET);

        createNotificationChannel();

        // Initialize PlayerNotificationManager
        playerNotificationManager = new PlayerNotificationManager.Builder(this, NOTIFICATION_ID, CHANNEL_ID)
                .setMediaDescriptionAdapter(new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(com.google.android.exoplayer2.Player player) {
                        return "Sample Video"; // Title in notification
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(com.google.android.exoplayer2.Player player) {
                        Intent intent = new Intent(PlayVideo23rdActivity.this, Main23rdActivity.class);
                        return PendingIntent.getActivity(PlayVideo23rdActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(com.google.android.exoplayer2.Player player) {
                        return "Playing a sample video"; // Subtitle in notification
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(com.google.android.exoplayer2.Player player, PlayerNotificationManager.BitmapCallback callback) {
                        return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground_23rd); // Icon in notification
                    }
                })
                .build();

        // Attach the player to the notification manager
        playerNotificationManager.setPlayer(player);

        playError();
    }

    private Uri getMediaUri(String filePath) {
        Uri fileUri = null;
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{filePath};

        // Query the MediaStore for the file URI
        Cursor cursor = getContentResolver().query(
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Playback Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Channel for playback controls");
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    private void playVideoSubtitle(Uri subtitle) {
        // Stop the current player
        if (player != null) {
            player.stop();
        }

        String path = mVideoFiles.get(position).getData();
        Uri videoUri = Uri.parse(path);

        // Create the player with a DefaultTrackSelector
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
        player = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build();
        playerView.setPlayer(player);

        // Create a DataSource factory
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "app"));

        // Build the MediaItem with subtitles
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(videoUri)
                .setSubtitleConfigurations(Collections.singletonList(
                        new MediaItem.SubtitleConfiguration.Builder(subtitle)
                                .setMimeType(MimeTypes.APPLICATION_SUBRIP) // Ensure correct MIME type
                                .setLanguage("en")
                                .setRoleFlags(C.ROLE_FLAG_SUBTITLE)
                                .build()))
                .build();

        // Set the media item and prepare the player
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        // Ensure subtitles are enabled
        trackSelector.setParameters(trackSelector.buildUponParameters().setPreferredTextLanguage("en"));

        // Keep the screen on
        playerView.setKeepScreenOn(true);
    }


    private void playError() {
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                // Handle the error based on its type or severity
                String errorMessage = "An error occurred during playback: " + error.getMessage();
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                // Optionally, stop the player or perform other actions
                player.stop();
            }
        });
        player.setPlayWhenReady(true);
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.eqFrameLayout);
        if (eqFrameLayout.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            if (fragment.isVisible() && eqFrameLayout.getVisibility() == View.VISIBLE) {
                eqFrameLayout.setVisibility(View.GONE);

            } else {
                if (player != null) {
                    player.release();
                }
                super.onBackPressed();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause() {
        super.onPause();


        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
            if (isInPictureInPictureMode()) {
                player.setPlayWhenReady(true);
            } else {
                player.setPlayWhenReady(false);
                player.getPlaybackState();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void hideBottomBar() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decodeView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decodeView.setSystemUiVisibility(uiOptions);
        }
    }

    View.OnClickListener firstListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scalingImageView.setImageResource(R.drawable.fullscreen_23rd);

            Toast.makeText(PlayVideo23rdActivity.this, "Full Screen", Toast.LENGTH_SHORT).show();
            scalingImageView.setOnClickListener(secondListener);
        }
    };

    View.OnClickListener secondListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scalingImageView.setImageResource(R.drawable.zoom_23rd);

            Toast.makeText(PlayVideo23rdActivity.this, "Zoom", Toast.LENGTH_SHORT).show();
            scalingImageView.setOnClickListener(thirdListener);
        }
    };
    View.OnClickListener thirdListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scalingImageView.setImageResource(R.drawable.fit_23rd);

            Toast.makeText(PlayVideo23rdActivity.this, "Fit", Toast.LENGTH_SHORT).show();
            scalingImageView.setOnClickListener(firstListener);
        }
    };

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        isCrossChecked = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            playerView.hideController();
        } else {
            playerView.showController();
            if (player != null && player.isPlaying()) {
                player.pause();
            }
        }
    }
}