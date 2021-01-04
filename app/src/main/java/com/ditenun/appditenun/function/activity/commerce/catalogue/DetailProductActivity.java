package com.ditenun.appditenun.function.activity.commerce.catalogue;

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

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDetailProductBinding;
import com.ditenun.appditenun.databinding.ItemProductColorBinding;
import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.models.ProductColor;
import com.ditenun.appditenun.function.activity.commerce.cart.CartActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;
    private DetailProductViewModel viewModel;
    private SimpleRecyclerAdapter<ProductColor> colorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_product);
        viewModel = ViewModelProviders.of(this).get(DetailProductViewModel.class);
        initLayout();
        observeLiveData();
        getAdditionalData();
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
            orderProduct();
        });
        binding.tvDetailDescription.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ProductDescriptionFragment productDescriptionFragment = new ProductDescriptionFragment();
            productDescriptionFragment.show(fragmentManager, "product_description_fragment");
        });
        binding.btnIncreaseQty.setOnClickListener(view -> viewModel.increaseProductQty());
        binding.btnDecreaseQty.setOnClickListener(view -> viewModel.decreaseProductQty());
        setupColorAdapter();
    }

    private void setupColorAdapter() {
        binding.rvColor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        colorAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_product_color, (holder, item) -> {

            ItemProductColorBinding itemBinding = (ItemProductColorBinding) holder.getLayoutBinding();

            switch (item.getColorName()) {
                case "RED": {
                    itemBinding.colorValue.setImageDrawable(getResources().getDrawable(R.color.colorRed));
                    break;
                }
                case "BLUE": {
                    itemBinding.colorValue.setImageDrawable(getResources().getDrawable(R.color.colorBlue));
                    break;
                }
                case "GREEN": {
                    itemBinding.colorValue.setImageDrawable(getResources().getDrawable(R.color.colorGreen));
                    break;
                }
            }

            if (item.isSelected()) {
                itemBinding.selectedColorIndicator.setVisibility(View.VISIBLE);
            } else {
                itemBinding.selectedColorIndicator.setVisibility(View.INVISIBLE);
            }

            itemBinding.colorValue.setOnClickListener(view -> {
                viewModel.setSelectedColor(item.getColorCode());
                colorAdapter.notifyDataSetChanged();
            });

        });
        binding.rvColor.setAdapter(colorAdapter);
        colorAdapter.setMainData(viewModel.getProductColorList());
        colorAdapter.notifyDataSetChanged();
    }

    private void observeLiveData() {
        viewModel.getSuccessGetDetailProduct().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                binding.lyProductImage.removeAllSliders();
                for (String imageUrl : viewModel.getProduct().getImageUrls()) {
                    if (imageUrl != null) {
                        TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                        textSliderView.image(imageUrl).setScaleType(BaseSliderView.ScaleType.CenterCrop);
                        binding.lyProductImage.addSlider(textSliderView);
                    }
                }
                binding.etSize.setText(viewModel.getProduct().getDimension());
                binding.tvProductNameAppBar.setText(viewModel.getProduct().getName());
                binding.tvProductName.setText(viewModel.getProduct().getName());
                binding.tvProductPrice.setText(TextUtil.getInstance().formatToRp(viewModel.getProduct().getPrice()));
                binding.tvProductQty.setText(viewModel.getProduct().getQty().toString());
            }
        });

        viewModel.getIncreasePurchaseQtyEvent().observe(this, qty -> binding.tvProductQty.setText(qty.toString()));
        viewModel.getDecreasePurchaseQtyEvent().observe(this, qty -> binding.tvProductQty.setText(qty.toString()));
    }

    private void orderProduct() {
        Order order = new Order();
        order.addProduct(viewModel.getProduct());
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);

    }
}