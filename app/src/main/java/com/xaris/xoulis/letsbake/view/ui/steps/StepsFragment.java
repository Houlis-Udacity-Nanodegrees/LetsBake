package com.xaris.xoulis.letsbake.view.ui.steps;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.databinding.FragmentStepsBinding;

import com.xaris.xoulis.letsbake.di.Injectable;

import javax.inject.Inject;

public class StepsFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private StepsViewModel stepsViewModel;

    private FragmentStepsBinding binding;

    private ExoPlayer exoPlayer;
    private long playerPosition = -1;
    private boolean playWhenReady = true;

    private final String PLAYER_POSITION_STATE = "player_pos_state";
    private final String PLAYER_PLAY_STATE = "player_play_state";

    public StepsFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
        binding.setLifecycleOwner(this);

        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION_STATE);
            playWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_STATE);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        boolean isLand = getResources().getBoolean(R.bool.is_landscape);

        if (isTablet)
            getActivity().findViewById(R.id.recipe_step_container).setVisibility(View.VISIBLE);

        if (!isTablet && !isLand) {
            binding.nextStepButton.setOnClickListener(v -> {
                if (stepsViewModel.getStepId().getValue() != null) {
                    releasePlayer();
                    stepsViewModel.setStepId(stepsViewModel.getStepId().getValue() + 1);
                }
            });

            binding.previousStepButton.setOnClickListener(v -> {
                if (stepsViewModel.getStepId().getValue() != null) {
                    releasePlayer();
                    stepsViewModel.setStepId(stepsViewModel.getStepId().getValue() - 1);
                }
            });
        }

        Bundle args = getArguments();
        if (args != null) {
            Recipe recipe = args.getParcelable("recipe");
            int stepId = args.getInt("stepId");
            observeViewModel(recipe, stepId);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        String videoUrl = stepsViewModel.getVideoUrl();
        if (!TextUtils.isEmpty(videoUrl))
            initPlayer(videoUrl);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (exoPlayer != null) {
            outState.putLong(PLAYER_POSITION_STATE, exoPlayer.getCurrentPosition());
            outState.putBoolean(PLAYER_PLAY_STATE, exoPlayer.getPlayWhenReady());
        }
    }


    private void observeViewModel(Recipe recipe, int stepId) {
        stepsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(StepsViewModel.class);
        stepsViewModel.setRecipe(recipe);
        stepsViewModel.setStepId(stepId);
        stepsViewModel.getStep().observe(this, step -> {
            if (step != null) {
                binding.setViewModel(stepsViewModel);
                String videoUrl = stepsViewModel.getVideoUrl();
                if (!TextUtils.isEmpty(videoUrl))
                    initPlayer(videoUrl);
            }
        });
    }

    private void initPlayer(String url) {
        if (exoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            binding.playerView.setPlayer(exoPlayer);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "LetsBake"), new DefaultBandwidthMeter());
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
            exoPlayer.prepare(videoSource);

            if (playerPosition != -1) {
                exoPlayer.seekTo(playerPosition);
                playerPosition = -1;
            }

            exoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
