package com.thomashorta.bakingtime;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thomashorta.bakingtime.model.Step;

public class StepDetailFragment extends Fragment {
    public static final String EXTRA_STEP = "step";
    public static final String EXTRA_SHOW_PREVIOUS = "show_previous";
    public static final String EXTRA_SHOW_NEXT = "show_next";

    private static final String VIDEO_EXT = ".mp4";

    private Step mStep;
    private boolean mShowPrevious;
    private boolean mShowNext;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(Step step, boolean showPrevious,
                                                 boolean showNext) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP, step);
        args.putBoolean(EXTRA_SHOW_PREVIOUS, showPrevious);
        args.putBoolean(EXTRA_SHOW_NEXT, showNext);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(EXTRA_STEP);
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
            rootView.findViewById(R.id.btn_previous)
                    .setVisibility(mShowPrevious ? View.VISIBLE : View.GONE);
            rootView.findViewById(R.id.btn_next)
                    .setVisibility(mShowNext ? View.VISIBLE : View.GONE);
        } else {
            rootView.findViewById(R.id.rl_button_bar).setVisibility(View.GONE);

        }

        TextView title = (TextView) rootView.findViewById(R.id.tv_step_detail_title);
        TextView desc = (TextView) rootView.findViewById(R.id.tv_step_detail_description);

        title.setText(String.format(getString(R.string.steps_title_format), mStep.getId() + 1));
        String description = mStep.getId() > 0 ? mStep.getDescription().substring(3)
                : mStep.getDescription();
        desc.setText(description);

        String url;
        if ((url = getVideoUrl()) != null) {
            // TODO: initialize exoplayer with content
        } else if ((url = getThumbnailUrl()) != null) {
            ImageView thumbnail = (ImageView) rootView.findViewById(R.id.iv_step_thumb);
            thumbnail.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(Uri.parse(url)).fit().centerCrop().into(thumbnail);
        } else {

        }

        return rootView;
    }

    private String getVideoUrl() {
        String videoUrl = mStep.getVideoURL();
        String thumbUrl = mStep.getThumbnailURL();
        if (!isEmpty(videoUrl) && videoUrl.toLowerCase().endsWith(VIDEO_EXT)) {
            return videoUrl;
        } else if (!isEmpty(thumbUrl) && thumbUrl.toLowerCase().endsWith(VIDEO_EXT)) {
            return thumbUrl;
        } else {
            return null;
        }
    }

    private String getThumbnailUrl() {
        String videoUrl = mStep.getVideoURL();
        String thumbUrl = mStep.getThumbnailURL();
        if (!isEmpty(thumbUrl) && !thumbUrl.toLowerCase().endsWith(VIDEO_EXT)) {
            return thumbUrl;
        } else if (!isEmpty(videoUrl) && !videoUrl.toLowerCase().endsWith(VIDEO_EXT)) {
            return videoUrl;
        } else {
            return null;
        }
    }

    private boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
