package com.newplayer.april23rd.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.model.TrendVideoData23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;
import com.newplayer.april23rd.utils.OnSwipeTouchListener123rd;


public class TrendingVideo23rdFragment extends Fragment implements Player.Listener {
    private static final String ARG_REEL = "reel";
    private TrendVideoData23rd trendVideoData23rd;
    private StyledPlayerView playerView;
    public TextView videoNameTextView, categoryNameTextView;
    private ExoPlayer player;
    ImageView thumb_image, downloadImageView, volumeImageView;
    ProgressBar p_bar;
    AdController23rd adController23rdGold;
    Activity activity;

    public static TrendingVideo23rdFragment newInstance(TrendVideoData23rd reel) {
        TrendingVideo23rdFragment fragment = new TrendingVideo23rdFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REEL, reel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trendVideoData23rd = getArguments().getParcelable(ARG_REEL);
        }

        activity = getActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reel_23rd, container, false);
        playerView = view.findViewById(R.id.playerview);
        thumb_image = view.findViewById(R.id.thumb_image);
        p_bar = view.findViewById(R.id.p_bar);
        videoNameTextView = view.findViewById(R.id.videoNameTextView);
        categoryNameTextView = view.findViewById(R.id.categoryNameTextView);
        downloadImageView = view.findViewById(R.id.downloadImageView);
        volumeImageView = view.findViewById(R.id.volumeImageView);

        adController23rdGold = AdController23rd.getInstance();

        initializePlayer();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 200);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    public static LoadControl getExoControler() {
        return new DefaultLoadControl.Builder()
                .setAllocator(new DefaultAllocator(true, (12 * 1024 * 1024)))
                .setBufferDurationsMs(1000, 5000, 1000, 1000)
                .setTargetBufferBytes(-1)
                .setPrioritizeTimeOverSizeThresholds(true)
                .build();
    }

    private void initializePlayer() {
        if (player == null && trendVideoData23rd != null) {
            try {
                player = new ExoPlayer.Builder(activity).
                        setTrackSelector(new DefaultTrackSelector(activity)).
                        setLoadControl(getExoControler()).
                        build();
                videoNameTextView.setText(trendVideoData23rd.getVideoTitle());
                categoryNameTextView.setText("# " + trendVideoData23rd.getCategoryName());

                Uri videoURI = Uri.parse(trendVideoData23rd.getVideoUrl());
                MediaItem mediaItem = MediaItem.fromUri(videoURI);
                player.setMediaItem(mediaItem);
                player.prepare();
                player.setRepeatMode(Player.REPEAT_MODE_ALL);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(C.USAGE_MEDIA)
                        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                        .build();
                player.setAudioAttributes(audioAttributes, true);
                player.addListener(this);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            playerView.setPlayer(player);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.removeListener(this);
            player.release();
            player = null;
        }
    }

    public void setData() {
        if (trendVideoData23rd != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(trendVideoData23rd.getThumbNailImage())
                    .placeholder(R.drawable.ic_logo_23rd)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumb_image);

            downloadImageView.setOnClickListener(v -> {
                if (trendVideoData23rd != null) {
                    adController23rdGold.showInterAdCallBack(activity, new AdController23rd.MyCallback() {
                        @Override
                        public void callbackCall() {
                            String folderName = "TrendingVideo";
                            FetchStorageVideo23rd.downloadVideo(activity, trendVideoData23rd.getVideoUrl(), folderName, System.currentTimeMillis() + "_video.mp4");
                        }
                    });
                }
            });
        }
    }


    public void setPlayer(boolean isVisibleToUser) {
        if (player != null) {
            if (player != null) {
                if (isVisibleToUser) {
                    player.setPlayWhenReady(true);
                } else {
                    player.setPlayWhenReady(false);
                    playerView.setAlpha(1);
                }
                playerView.setOnTouchListener(new OnSwipeTouchListener123rd(activity) {

                    public void onSwipeLeft() {

                    }

                    @Override
                    public void onLongClick() {
                    }

                    @Override
                    public void onSingleClick() {
                        if (isVisibleToUser) {
                            if (!player.getPlayWhenReady()) {
                                player.setPlayWhenReady(true);
                                volumeImageView.setVisibility(View.VISIBLE);
                                volumeImageView.setImageResource(R.drawable.ic_volume_23rd);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        volumeImageView.setVisibility(View.GONE);
                                    }
                                }, 500);
                            } else {
                                player.setPlayWhenReady(false);
                                volumeImageView.setVisibility(View.VISIBLE);
                                volumeImageView.setImageResource(R.drawable.ic_mute_23rd);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        volumeImageView.setVisibility(View.GONE);
                                    }
                                }, 500);
                            }
                        }
                    }

                    @Override
                    public void onDoubleClick(MotionEvent e) {

                    }

                });

            }

        }
    }

    @Override
    public void onPlaybackStateChanged(int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            p_bar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            thumb_image.setVisibility(View.GONE);

            p_bar.setVisibility(View.INVISIBLE);
        } else if (playbackState == Player.STATE_ENDED) {

        }
    }

    boolean isVisibleToUser;

    @Override
    public void setMenuVisibility(final boolean visible) {
        isVisibleToUser = visible;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if (player != null && visible) {
                    setPlayer(isVisibleToUser);
                }

            }
        }, 200);

    }


    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
            playerView.setAlpha(1);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
            playerView.setAlpha(1);
        }
    }


}
