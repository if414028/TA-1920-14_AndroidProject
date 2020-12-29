package com.ditenun.appditenun.function.activity.commerce.dashboard.product;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ItemNewArrivalsBinding;
import com.ditenun.appditenun.databinding.ProductFragmentBinding;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.activity.commerce.catalogue.DetailProductActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    private ProductViewModel viewModel;
    private ProductFragmentBinding binding;

    private SimpleRecyclerAdapter<Product> productAdapter;

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.product_fragment, container, false);

        initLayout();
        observeLiveData();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.clearProductList();
        viewModel.fetchListProduct();
    }

    private void initLayout() {
        initProductRecyclerView();
    }

    private void initProductRecyclerView() {
        binding.rvProduct.setLayoutManager(new GridLayoutManager(getContext(), 3));
        productAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_new_arrivals, (holder, item) -> {
            ItemNewArrivalsBinding itemBinding = (ItemNewArrivalsBinding) holder.getLayoutBinding();
            if (item != null) {
                if (item.getImageUrls() != null) {
                    Picasso.with(getContext()).load(item.getImageUrls().get(0)).into(itemBinding.imgNewArrivals);
                }
            }
            itemBinding.tvProductName.setText(item.getName());
            itemBinding.tvProductPrice.setText(TextUtil.getInstance().formatToRp(item.getPrice()));
            itemBinding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DetailProductActivity.class);
                intent.putExtra("product", item);
                startActivity(intent);
            });
        });
        binding.rvProduct.setAdapter(productAdapter);
    }

    private void observeLiveData() {
        viewModel.getSuccessGetListProductEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                productAdapter.setMainData(viewModel.getProductList());
                productAdapter.notifyDataSetChanged();
            }
        });
    }

}