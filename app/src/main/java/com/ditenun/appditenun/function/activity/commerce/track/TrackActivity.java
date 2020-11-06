package com.ditenun.appditenun.function.activity.commerce.track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityTrackBinding;

public class TrackActivity extends AppCompatActivity {

    private ActivityTrackBinding binding;
    private TrackViewModel vIewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_track);
        vIewModel = ViewModelProviders.of(this).get(TrackViewModel.class);


    }

    private void initLayout() {

    }
}