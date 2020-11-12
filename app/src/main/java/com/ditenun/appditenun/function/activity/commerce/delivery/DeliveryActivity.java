package com.ditenun.appditenun.function.activity.commerce.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDeliveryBinding;
import com.ditenun.appditenun.databinding.ItemOrderBinding;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.activity.commerce.payment.PaymentActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeliveryActivity extends AppCompatActivity {

    private ActivityDeliveryBinding binding;
    private DeliveryViewModel viewModel;

    private SimpleRecyclerAdapter<Product> productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery);
        viewModel = ViewModelProviders.of(this).get(DeliveryViewModel.class);

        getAdditionalData();
        initLayout();
    }

    private void getAdditionalData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("productList")) {
                viewModel.setProductList(intent.getParcelableArrayListExtra("productList"));
            }
        }
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        binding.btnPay.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
            startActivity(intent);
        });
        initProductRecyclerView();
    }

    private void initProductRecyclerView() {
        binding.rvOrder.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        productAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_order, (holder, item) -> {

            ItemOrderBinding itemBinding = (ItemOrderBinding) holder.getLayoutBinding();
            itemBinding.tvProductName.setText(item.getName());
            itemBinding.tvPrice.setText(TextUtil.getInstance().formatToRp(item.getPrice()));
            itemBinding.tvSubTotalPrice.setText("Total: " + TextUtil.getInstance().formatToRp(item.getPrice() * item.getQty()));
            Picasso.with(getApplicationContext()).load(item.getImageUrls().get(0)).into(itemBinding.imgProduct);
        });
        binding.rvOrder.setAdapter(productAdapter);
        productAdapter.setMainData(viewModel.getProductList());
    }
}