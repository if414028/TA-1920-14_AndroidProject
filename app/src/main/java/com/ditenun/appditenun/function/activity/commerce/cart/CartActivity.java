package com.ditenun.appditenun.function.activity.commerce.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityCartBinding;
import com.ditenun.appditenun.databinding.ItemCartBinding;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.activity.commerce.delivery.DeliveryActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartViewModel viewModel;
    private SimpleRecyclerAdapter<Product> cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        viewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        getAdditionalData();
        initLayout();
        observeLiveData();
    }

    private void getAdditionalData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("product")) {
                viewModel.addProduct(intent.getParcelableExtra("product"));
            }
        }
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DeliveryActivity.class);
            intent.putParcelableArrayListExtra("productList", (ArrayList<? extends Parcelable>) viewModel.getProductList());
            startActivity(intent);
        });
        initCartRecyclerView();
    }

    private void initCartRecyclerView() {
        binding.rvCart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        cartAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_cart, (holder, item) -> {
            ItemCartBinding itemBinding = (ItemCartBinding) holder.getLayoutBinding();
            itemBinding.tvProductName.setText(item.getName());
            itemBinding.tvSize.setText(item.getFeature());
            itemBinding.tvPrice.setText(TextUtil.getInstance().formatToRp(item.getPrice()));
            itemBinding.etProductQty.setText(item.getQty().toString());
            Picasso.with(getApplicationContext()).load(item.getImageUrls().get(0)).into(itemBinding.imgProduct);
        });
        binding.rvCart.setAdapter(cartAdapter);
    }

    private void observeLiveData() {
        viewModel.getProductListLiveData().observe(this, products -> {
            cartAdapter.setMainData(products);
            binding.tvTotalPrice.setText(TextUtil.getInstance().formatToRp(viewModel.calculateTotalPrice()));
        });
    }
}