package com.ditenun.appditenun.function.activity.commerce.order.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityListOrderBinding;
import com.ditenun.appditenun.databinding.ItemMyOrderBinding;
import com.ditenun.appditenun.databinding.ItemNewArrivalsBinding;
import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.activity.commerce.order.detail.DetailOrderActivity;
import com.ditenun.appditenun.function.activity.commerce.order.track.TrackOrderActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ListOrderActivity extends AppCompatActivity {

    private ActivityListOrderBinding binding;
    private ListOrderViewModel viewModel;

    private SimpleRecyclerAdapter<Order> orderAdapter;
    private SimpleRecyclerAdapter<Product> productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_order);
        viewModel = ViewModelProviders.of(this).get(ListOrderViewModel.class);

        initLayout();
        observeLiveData();

        viewModel.fetchListOrder();
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        initOrderRecyclerView();
    }

    private void initOrderRecyclerView() {
        binding.rvOrder.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        orderAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_my_order, (holder, item) -> {
            ItemMyOrderBinding itemBinding = (ItemMyOrderBinding) holder.getLayoutBinding();

            itemBinding.tvOrderNumber.setText(String.format("Nomor Pesanan : %s", item.getOrderNo()));
            itemBinding.tvOrderDate.setText(getReadableDateFormat(Long.parseLong(item.getOrderDate())));

            itemBinding.rvProduct.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
            productAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_new_arrivals, (productHolder, productItem) -> {
                ItemNewArrivalsBinding productItemBinding = (ItemNewArrivalsBinding) productHolder.getLayoutBinding();
                productItemBinding.lyPrice.setVisibility(View.GONE);
                if (productItem != null) {
                    if (productItem.getImageUrls() != null) {
                        Picasso.with(getApplicationContext()).load(productItem.getImageUrls().get(0)).into(productItemBinding.imgNewArrivals);
                    }
                }
            });
            itemBinding.rvProduct.setAdapter(productAdapter);
            productAdapter.setMainData(item.getProduct());
            productAdapter.notifyDataSetChanged();

            itemBinding.btnDetailOrder.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), DetailOrderActivity.class);
                intent.putExtra("order", item);
                startActivity(intent);
            });
            itemBinding.btnTrackOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), TrackOrderActivity.class);
                    intent.putExtra("order", item);
                    startActivity(intent);
                }
            });
        });
        binding.rvOrder.setAdapter(orderAdapter);
    }

    private void observeLiveData() {
        viewModel.getSuccessGetOrderList().observe(this, aVoid -> {
            orderAdapter.setMainData(viewModel.getOrderList());
            orderAdapter.notifyDataSetChanged();
        });
        viewModel.getErrorGetOrderList().observe(this, new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError databaseError) {

            }
        });
    }

    private String getReadableDateFormat(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd MMM yyyy", cal).toString();
        return date;
    }
}