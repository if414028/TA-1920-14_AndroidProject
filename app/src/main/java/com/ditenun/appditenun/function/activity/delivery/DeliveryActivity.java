package com.ditenun.appditenun.function.activity.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDeliveryBinding;

public class DeliveryActivity extends AppCompatActivity {

    private ActivityDeliveryBinding binding;
    private DeliveryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery);
        viewModel = ViewModelProviders.of(this).get(DeliveryViewModel.class);
    }
}