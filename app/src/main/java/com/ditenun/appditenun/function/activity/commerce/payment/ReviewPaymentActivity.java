package com.ditenun.appditenun.function.activity.commerce.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityReviewPaymentBinding;
import com.ditenun.appditenun.databinding.ItemReviewPaymentProductBinding;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.activity.commerce.dashboard.DashboardActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReviewPaymentActivity extends AppCompatActivity {

    private ActivityReviewPaymentBinding binding;
    private ReviewPaymentViewModel viewModel;

    private SimpleRecyclerAdapter<Product> productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_payment);
        viewModel = ViewModelProviders.of(this).get(ReviewPaymentViewModel.class);

        initLayout();
        observeLivedata();
        getAdditionalData();
    }

    private void getAdditionalData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("order")) {
                viewModel.setOrder(intent.getParcelableExtra("order"));
            }
        }
    }

    private void initLayout() {
        initProductRecyclerView();
        binding.btnOrderAgain.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        binding.btnOpenListOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(DashboardActivity.ARG_SUCCESS_CREATE_ORDER, true);
                startActivity(intent);
            }
        });
    }

    private void initProductRecyclerView() {
        binding.rvProduct.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        productAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_review_payment_product, (holder, item) -> {
            ItemReviewPaymentProductBinding itemBinding = (ItemReviewPaymentProductBinding) holder.getLayoutBinding();

            itemBinding.tvProductName.setText(item.getName());
            itemBinding.tvQty.setText(String.format("Jumlah %s", TextUtil.getInstance().formatToRp(item.getPriceInDouble())));
            itemBinding.tvPrice.setText(String.format("%s / Unit", TextUtil.getInstance().formatToRp(item.getPriceInDouble() * item.getPurchasedStock())));
            Picasso.with(getApplicationContext()).load(item.getImages().get(0).getSrc()).into(itemBinding.imgProduct);
        });
        binding.rvProduct.setAdapter(productAdapter);
    }

    private void observeLivedata() {
        viewModel.getSuccessGetOrderEvent().observe(this, aVoid -> {
            productAdapter.setMainData(viewModel.getOrder().getProduct());
            productAdapter.notifyDataSetChanged();

            binding.tvOrderNumberValue.setText(viewModel.getOrder().getOrderNo());
            binding.tvOrderDateValue.setText(getReadableDateFormat(Long.parseLong(viewModel.getOrder().getOrderDate())));
            binding.tvPaymentStatusValue.setText(viewModel.getOrder().getPaymentStatus());
            binding.tvPaymentMethodValue.setText(viewModel.getOrder().getPaymentMethod().getPaymentName());
        });
    }

    private String getReadableDateFormat(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd MMM yyyy", cal).toString();
        return date;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}