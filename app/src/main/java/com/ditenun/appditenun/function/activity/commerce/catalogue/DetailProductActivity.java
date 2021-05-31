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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDetailProductBinding;
import com.ditenun.appditenun.databinding.ItemProductColorBinding;
import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.models.ProductColor;
import com.ditenun.appditenun.dependency.models.ProductImages;
import com.ditenun.appditenun.dependency.modules.WooCommerceApiClient;
import com.ditenun.appditenun.dependency.network.WooCommerceApiInterface;
import com.ditenun.appditenun.function.activity.commerce.cart.CartActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;
    private DetailProductViewModel viewModel;
    private SimpleRecyclerAdapter<String> colorAdapter;
    private ArrayAdapter<String> sizeAdapter;

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
            if (intent.hasExtra("product_id")) {
                viewModel.fetchDetailProduct(intent.getIntExtra("product_id", 0));
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
        setupSizeSpinner();
    }

    private void setupColorAdapter() {
        binding.rvColor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        colorAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_product_color, (holder, item) -> {

            ItemProductColorBinding itemBinding = (ItemProductColorBinding) holder.getLayoutBinding();

            switch (item) {
                case "Merah": {
                    itemBinding.colorValue.setImageDrawable(getResources().getDrawable(R.color.colorRed));
                    break;
                }
                case "Biru": {
                    itemBinding.colorValue.setImageDrawable(getResources().getDrawable(R.color.colorBlue));
                    break;
                }
                case "Hijau": {
                    itemBinding.colorValue.setImageDrawable(getResources().getDrawable(R.color.colorGreen));
                    break;
                }
                case "Kuning": {
                    itemBinding.colorValue.setImageDrawable(getResources().getDrawable(R.color.colorYellow));
                    break;
                }
                case "Hitam": {
                    itemBinding.colorValue.setImageDrawable(getResources().getDrawable(R.color.black));
                }
            }

            if (item.equalsIgnoreCase(viewModel.getSelectedColor())) {
                itemBinding.selectedColorIndicator.setVisibility(View.VISIBLE);
            } else {
                itemBinding.selectedColorIndicator.setVisibility(View.INVISIBLE);
            }

            itemBinding.colorValue.setOnClickListener(view -> {
                viewModel.setSelectedColor(item);
                colorAdapter.notifyDataSetChanged();
            });

        });
        binding.rvColor.setAdapter(colorAdapter);
    }

    private void setupSizeSpinner() {
        sizeAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, new ArrayList<>());
        binding.spinnerSize.setAdapter(sizeAdapter);
        binding.spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.setSelectedSize(sizeAdapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void observeLiveData() {
        viewModel.getSuccessGetDetailProduct().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                binding.lyProductImage.removeAllSliders();
                for (ProductImages imageUrl : viewModel.getProduct().getImages()) {
                    if (imageUrl != null) {
                        TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                        textSliderView.image(imageUrl.getSrc()).setScaleType(BaseSliderView.ScaleType.CenterInside);
                        binding.lyProductImage.addSlider(textSliderView);
                    }
                }
                binding.tvProductNameAppBar.setText(viewModel.getProduct().getName());
                binding.tvProductName.setText(viewModel.getProduct().getName());
                binding.tvProductPrice.setText(TextUtil.getInstance().formatToRp(viewModel.getProduct().getPriceInDouble()));
                binding.tvProductQty.setText(viewModel.getProduct().getStockQuantity().toString());
                setColorList();
                setSizeList();
            }
        });

        viewModel.getIncreasePurchaseQtyEvent().observe(this, qty -> binding.tvProductQty.setText(qty.toString()));
        viewModel.getDecreasePurchaseQtyEvent().observe(this, qty -> binding.tvProductQty.setText(qty.toString()));
    }

    private void setColorList() {
        if (viewModel.getProductColorList().size() > 0) {
            binding.lyColorPicker.setVisibility(View.VISIBLE);
            colorAdapter.setMainData(viewModel.getProductColorList());
            colorAdapter.notifyDataSetChanged();
        } else {
            binding.lyColorPicker.setVisibility(View.GONE);
        }
    }

    private void setSizeList() {
        if (viewModel.getProductSizeList().size() > 0) {
            sizeAdapter.addAll(viewModel.getProductSizeList());
            sizeAdapter.notifyDataSetChanged();
        }
    }

    private void orderProduct() {
        Order order = new Order();
        order.addProduct(viewModel.getProduct());
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);

    }
}