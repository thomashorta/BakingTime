package com.thomashorta.bakingtime;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thomashorta.bakingtime.model.Step;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            Step step = getIntent().getParcelableExtra(StepDetailFragment.EXTRA_STEP);
            boolean showPrevious = getIntent()
                    .getBooleanExtra(StepDetailFragment.EXTRA_SHOW_PREVIOUS, false);
            boolean showNext = getIntent()
                    .getBooleanExtra(StepDetailFragment.EXTRA_SHOW_NEXT, false);

            StepDetailFragment fragment = StepDetailFragment.newInstance(step,
                    showPrevious, showNext);

            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }
}
