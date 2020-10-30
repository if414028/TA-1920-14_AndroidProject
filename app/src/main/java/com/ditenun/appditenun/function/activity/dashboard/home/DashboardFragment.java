package com.ditenun.appditenun.function.activity.dashboard.home;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.DashboardFragmentBinding;
import com.ditenun.appditenun.function.activity.HomeActivity;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    private DashboardFragmentBinding binding;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false);

        initLayout();

        return binding.getRoot();
    }

    private void initLayout() {
        binding.btnDesign.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
        });
    }

}