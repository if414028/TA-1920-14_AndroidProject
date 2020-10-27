package com.ditenun.appditenun.function.activity.dashboard.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDetailProductBinding;
import com.ditenun.appditenun.function.activity.cart.CartActivity;

public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;
    private DetailProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_product);
        viewModel = ViewModelProviders.of(this).get(DetailProductViewModel.class);
        initLayout();
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }
}