package com.thomashorta.bakingtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.thomashorta.bakingtime.model.Step;

public class StepDetailFragment extends Fragment {
    private static final String TAG = StepDetailFragment.class.getSimpleName();

    public static final String EXTRA_STEP = "step";
    public static final String EXTRA_STEP_NUMBER = "step_number";
    public static final String EXTRA_SHOW_PREVIOUS = "show_previous";
    public static final String EXTRA_SHOW_NEXT = "show_next";
    public static final String EXTRA_PLAY_WHEN_READY = "play_when_ready";
    public static final String EXTRA_VIDEO_POSITION = "video_position";

    private Step mStep;
    private int mStepNumber;
    private boolean mShowPrevious;
    private boolean mShowNext;

    private SimpleExoPlayerView mExoPlayerView;
    private ScrollView mDetailView;

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;
    private MediaReceiver mMediaReceiver;

    private OnStepButtonClickListener mButtonCallback;

    public interface OnStepButtonClickListener {
        void onClickPrevious();
        void onClickNext();
    }

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(Step step, int stepNumber,
                                                 boolean showPrevious, boolean showNext) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP, step);
        args.putInt(EXTRA_STEP_NUMBER, stepNumber);
        args.putBoolean(EXTRA_SHOW_PREVIOUS, showPrevious);
        args.putBoolean(EXTRA_SHOW_NEXT, showNext);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepButtonClickListener) {
            mButtonCallback = (OnStepButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecipeDetailAdapter.OnStepClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(EXTRA_STEP);
            mStepNumber = getArguments().getInt(EXTRA_STEP_NUMBER);
            mShowPrevious = getArguments().getBoolean(EXTRA_SHOW_PREVIOUS);
            mShowNext = getArguments().getBoolean(EXTRA_SHOW_NEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        if (mShowNext || mShowPrevious) {
            rootView.findViewById(R.id.rl_button_bar).setVisibility(View.VISIBLE);
            Button btnPrevious = (Button) rootView.findViewById(R.id.btn_previous);
            btnPrevious.setVisibility(mShowPrevious ? View.VISIBLE : View.GONE);
            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mButtonCallback.onClickPrevious();
                }
            });

            Button btnNext = (Button) rootView.findViewById(R.id.btn_next);
            btnNext.setVisibility(mShowNext ? View.VISIBLE : View.GONE);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mButtonCallback.onClickNext();
                }
            });
        } else {
            rootView.findViewById(R.id.rl_button_bar).setVisibility(View.GONE);
        }

        mDetailView = (ScrollView) rootView.findViewById(R.id.sv_details);
        TextView title = (TextView) rootView.findViewById(R.id.tv_step_detail_title);
        TextView desc = (TextView) rootView.findViewById(R.id.tv_step_detail_description);

        title.setText(String.format(getString(R.string.steps_title_format), mStepNumber));
        String description = mStep.getId() > 0 ? mStep.getDescription().substring(3)
                : mStep.getDescription();
        desc.setText(description);

        String url;
        if ((url = mStep.getVideoURL()) != null) {
            mExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.ep_step_video);

            mMediaReceiver = new MediaReceiver();
            getContext().registerReceiver(mMediaReceiver,
                    new IntentFilter(Intent.ACTION_MEDIA_BUTTON));

            initializeMediaSession();
            boolean playWhenReady = false;
            long position = 0;
            if (savedInstanceState != null) {
                playWhenReady = savedInstanceState.getBoolean(EXTRA_PLAY_WHEN_READY);
                position = savedInstanceState.getLong(EXTRA_VIDEO_POSITION);
            }
            initializePlayer(Uri.parse(url), playWhenReady, position);
            mExoPlayerView.setVisibility(View.VISIBLE);
        } else if ((url = mStep.getThumbnailURL()) != null) {
            ImageView thumbnail = (ImageView) rootView.findViewById(R.id.iv_step_thumb);
            thumbnail.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(Uri.parse(url)).fit().centerCrop().into(thumbnail);
        }

        displayLayoutForOrientation(getResources().getConfiguration().orientation);

        return rootView;
    }

    private boolean mWasPlayingOnPause = false;

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mWasPlayingOnPause = mExoPlayer.getPlayWhenReady();
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null) {
            outState.putBoolean(EXTRA_PLAY_WHEN_READY, mWasPlayingOnPause);
            outState.putLong(EXTRA_VIDEO_POSITION, mExoPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        if (mMediaReceiver != null) getContext().unregisterReceiver(mMediaReceiver);
        if (mMediaSession != null) mMediaSession.setActive(false);
        releasePlayer();
        super.onDestroyView();
    }

    public void displayLayoutForOrientation(int orientation) {
        if (mExoPlayerView == null) return;

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
                mExoPlayerView.getLayoutParams();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mDetailView.setVisibility(View.GONE);
            lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
            lp.height = 0;
            lp.weight = 1;
            lp.bottomMargin = 0;
            mExoPlayerView.setLayoutParams(lp);
        } else {
            mDetailView.setVisibility(View.VISIBLE);
            lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
            lp.height = (int) getResources().getDimension(R.dimen.content_height);
            lp.bottomMargin = (int) getResources().getDimension(R.dimen.vertical_spacing);
            mExoPlayerView.setLayoutParams(lp);
        }
    }

    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUrl, boolean playWhenReady, long position) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(mExoPlayerListener);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUrl,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.seekTo(position);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    // ExoPlayer Event Listener
    private ExoPlayer.EventListener mExoPlayerListener = new ExoPlayer.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        mExoPlayer.getCurrentPosition(), 1f);
            } else if ((playbackState == ExoPlayer.STATE_READY)) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        mExoPlayer.getCurrentPosition(), 1f);
            }
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
        }

        @Override
        public void onPositionDiscontinuity() {
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }
    };

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
