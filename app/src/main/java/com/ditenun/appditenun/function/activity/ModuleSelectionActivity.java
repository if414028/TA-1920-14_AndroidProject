package com.ditenun.appditenun.function.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityModuleSelectionBinding;
import com.ditenun.appditenun.function.activity.commerce.dashboard.DashboardActivity;

public class ModuleSelectionActivity extends AppCompatActivity {

    private ActivityModuleSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_module_selection);

        initLayout();
    }

    private void initLayout() {
        binding.btnDesignModule.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        binding.btnEcommerceModule.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
        });
    }
}