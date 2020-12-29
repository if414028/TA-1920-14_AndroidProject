package com.ditenun.appditenun.function.activity.commerce.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityPaymentBinding;
import com.ditenun.appditenun.databinding.ItemPaymentMethodBinding;
import com.ditenun.appditenun.dependency.models.PaymentMethod;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;

import org.w3c.dom.Text;

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
        observeLiveData();
        getAdditionalData();

        viewModel.fetchPaymentMethodList();
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        binding.btnPay.setOnClickListener(view -> viewModel.submitOrder());
        initPaymentRecyclerView();
    }

    private void initPaymentRecyclerView() {
        binding.rvPaymentMethod.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        paymentMethodAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_payment_method, (holder, item) -> {

            ItemPaymentMethodBinding itemBinding = (ItemPaymentMethodBinding) holder.getLayoutBinding();
            itemBinding.setModel(item);
            itemBinding.icSelectedIndicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setSelected(!item.isSelected());
                    paymentMethodAdapter.notifyDataSetChanged();

                    if (item.isSelected()) viewModel.setPaymentMethod(item);
                    else viewModel.setPaymentMethod(null);

                    refreshLayout();
                }
            });

        });
        binding.rvPaymentMethod.setAdapter(paymentMethodAdapter);
    }

    private void getAdditionalData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("order")) {
                viewModel.setOrder(intent.getParcelableExtra("order"));
            }
        }
    }

    private void refreshLayout() {
        binding.tvTotalPrice.setText(TextUtil.getInstance().formatToRp(viewModel.calculateNettTotalPrice()));
        binding.tvSubTotalPrice.setText(TextUtil.getInstance().formatToRp(viewModel.calculateTotalOrderPrice()));
        binding.tvShippingPrice.setText(TextUtil.getInstance().formatToRp(viewModel.getOrder().getShipping().getShipPrice()));
        binding.tvTotalValue.setText(TextUtil.getInstance().formatToRp(viewModel.calculateNettTotalPrice()));
        binding.tvSelectedPaymentMethod.setText(viewModel.getOrder().getPaymentMethod() != null ? "(" + viewModel.getOrder().getPaymentMethod().getPaymentName() + ")" : "-");
        validateSubmitButton();
    }

    private void observeLiveData() {
        viewModel.getSuccessGetOrderEvent().observe(this, aVoid -> {
            refreshLayout();
        });

        viewModel.getSuccessGetPaymentMethodList().observe(this, aVoid -> {
            paymentMethodAdapter.setMainData(viewModel.getPaymentMethodList());
            paymentMethodAdapter.notifyDataSetChanged();
        });

        viewModel.getSuccessSubmitOrder().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(), ReviewPaymentActivity.class);
                intent.putExtra("order", viewModel.getOrder());
                startActivity(intent);
            }
        });
    }

    private void validateSubmitButton() {
        binding.btnPay.setEnabled(viewModel.getOrder().getPaymentMethod() != null);
    }
}