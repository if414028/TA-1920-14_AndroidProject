package com.ditenun.appditenun.function.activity.commerce.catalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDetailProductBinding;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.activity.commerce.cart.CartActivity;
import com.ditenun.appditenun.function.util.TextUtil;
import com.squareup.picasso.Picasso;

public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;
    private DetailProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_product);
        viewModel = ViewModelProviders.of(this).get(DetailProductViewModel.class);
        getAdditionalData();
        initLayout();
        observeLiveData();
    }

    private void getAdditionalData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("product")) {
                viewModel.setProduct(intent.getParcelableExtra("product"));
            }
        }

    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnBuy.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            intent.putExtra("product", viewModel.getProduct().getValue());
            startActivity(intent);
        });
        binding.tvDetailDescription.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ProductDescriptionFragment productDescriptionFragment = new ProductDescriptionFragment();
            productDescriptionFragment.show(fragmentManager, "product_description_fragment");
        });
        binding.btnIncreaseQty.setOnClickListener(view -> viewModel.increaseProductQty());
        binding.btnDecreaseQty.setOnClickListener(view -> viewModel.decreaseProductQty());
    }

    private void observeLiveData() {
        viewModel.getProduct().observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                Picasso.with(getApplicationContext()).load(product.getImageUrls().get(0)).into(binding.lyProductImage);
                binding.tvProductName.setText(product.getName());
                binding.tvProductPrice.setText(TextUtil.getInstance().formatToRp(product.getPrice()));
                binding.tvProductQty.setText(product.getQty().toString());
            }
        });
    }
}