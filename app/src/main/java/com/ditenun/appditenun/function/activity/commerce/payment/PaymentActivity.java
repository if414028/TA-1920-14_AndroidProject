package com.ditenun.appditenun.function.activity.commerce.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityPaymentBinding;
import com.ditenun.appditenun.databinding.ItemPaymentMethodBinding;
import com.ditenun.appditenun.dependency.models.PaymentMethod;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding binding;
    private PaymentViewModel viewModel;

    private SimpleRecyclerAdapter<PaymentMethod> paymentMethodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        viewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);

        initLayout();
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        initPaymentRecyclerView();
    }

    private void initPaymentRecyclerView() {
        binding.rvPaymentMethod.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        paymentMethodAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_payment_method, (holder, item) -> {

            ItemPaymentMethodBinding itemBinding = (ItemPaymentMethodBinding) holder.getLayoutBinding();
            itemBinding.setModel(item);

        });
    }
}