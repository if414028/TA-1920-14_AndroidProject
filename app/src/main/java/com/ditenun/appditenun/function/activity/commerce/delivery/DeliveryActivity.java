package com.ditenun.appditenun.function.activity.commerce.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDeliveryBinding;
import com.ditenun.appditenun.databinding.ItemOrderBinding;
import com.ditenun.appditenun.databinding.ItemPaymentMethodBinding;
import com.ditenun.appditenun.databinding.ItemShippingMethodBinding;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.models.Shipping;
import com.ditenun.appditenun.function.activity.commerce.catalogue.ProductDescriptionFragment;
import com.ditenun.appditenun.function.activity.commerce.payment.PaymentActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DeliveryActivity extends AppCompatActivity {

    private ActivityDeliveryBinding binding;
    private DeliveryViewModel viewModel;

    private SimpleRecyclerAdapter<Product> productAdapter;
    private SimpleRecyclerAdapter<Shipping> shippingMethodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery);
        viewModel = ViewModelProviders.of(this).get(DeliveryViewModel.class);

        getAdditionalData();
        initLayout();
        observeLiveEvent();

        viewModel.getAllShippingMethod();
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
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        binding.btnPay.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
            intent.putExtra("order", viewModel.getOrder());
            startActivity(intent);
        });
        binding.etDeliveryAddress.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            AddAddressFragment addAddressFragment = new AddAddressFragment();
            addAddressFragment.show(fragmentManager, "add_address_fragment");
        });
        binding.tvTotalOrder.setText(TextUtil.getInstance().formatToRp(viewModel.calculateTotalOrderPrice()));
        binding.tvTotalPayment.setText(TextUtil.getInstance().formatToRp(viewModel.calculateNettTotalPrice()));
        binding.tvTotalValue.setText(TextUtil.getInstance().formatToRp(viewModel.calculateNettTotalPrice()));
        initProductRecyclerView();
        initShippingMethodRecyclerView();
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
        productAdapter.setMainData(viewModel.getOrder().getProduct());
    }

    private void refreshLayout(){
        binding.tvTotalOrder.setText(TextUtil.getInstance().formatToRp(viewModel.calculateTotalOrderPrice()));
        binding.tvTotalPayment.setText(TextUtil.getInstance().formatToRp(viewModel.calculateNettTotalPrice()));
        binding.tvTotalValue.setText(TextUtil.getInstance().formatToRp(viewModel.calculateNettTotalPrice()));
        binding.tvShippingPrice.setText(viewModel.getOrder().getShipping() != null ? TextUtil.getInstance().formatToRp(viewModel.getOrder().getShipping().getShipPrice()) : "-");
        validateSubmitButton();
    }

    private void validateSubmitButton(){
        binding.btnPay.setEnabled(viewModel.getOrder().getShipping() != null && viewModel.getOrder().getAddress() != null);
    }

    private void initShippingMethodRecyclerView() {
        binding.rvDeliveryMethod.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        shippingMethodAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_shipping_method, (holder, item) -> {
            ItemShippingMethodBinding itemBinding = (ItemShippingMethodBinding) holder.getLayoutBinding();
            itemBinding.setModel(item);
            itemBinding.setTextUtil(TextUtil.getInstance());
            itemBinding.icSelectedIndicator.setOnClickListener(view -> {
                item.setSelected(!item.isSelected());
                shippingMethodAdapter.notifyDataSetChanged();

                if (item.isSelected()) viewModel.selectShippingMethod(item);
                else viewModel.selectShippingMethod(null);

                refreshLayout();
            });
        });
        binding.rvDeliveryMethod.setAdapter(shippingMethodAdapter);
    }

    private void observeLiveEvent() {
        viewModel.getSubmitAddressEvent().observe(this, s -> binding.etDeliveryAddress.setText(s));
        viewModel.getSuccessGetListShippingMethod().observe(this, aVoid -> {
            shippingMethodAdapter.setMainData(viewModel.getListShippingMethod());
            shippingMethodAdapter.notifyDataSetChanged();
            refreshLayout();
        });
        viewModel.getErrorGetListShippingMethod().observe(this, new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError databaseError) {

            }
        });
    }
}