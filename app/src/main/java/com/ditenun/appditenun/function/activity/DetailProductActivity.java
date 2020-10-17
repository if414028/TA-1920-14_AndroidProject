package com.ditenun.appditenun.function.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDetailProductBinding;

public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_product);

        initLayout();
    }

    private void initLayout(){
        binding.btnBack.setOnClickListener(v -> onBackPressed());
    }
}