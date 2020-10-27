package com.ditenun.appditenun.function.activity.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityCartBinding;
import com.ditenun.appditenun.databinding.ItemCartBinding;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartViewModel viewModel;
    private SimpleRecyclerAdapter<Product> cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        viewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        initLayout();
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initCartRecyclerView();
    }

    private void initCartRecyclerView() {
        binding.rvCart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        cartAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_cart, (holder, item) -> {
            ItemCartBinding itemBinding = (ItemCartBinding) holder.getLayoutBinding();
            itemBinding.tvProductName.setText(item.getProductName());
            itemBinding.tvSize.setText(item.getProductSize());
            itemBinding.tvPrice.setText(item.getProductPrice().toString());
            itemBinding.etProductQty.setText(item.getProductQty().toString());
            binding.tvTotalPrice.setText(item.getProductPrice().toString());
        });
        binding.rvCart.setAdapter(cartAdapter);
        cartAdapter.setMainData(viewModel.getProductList());
    }
}