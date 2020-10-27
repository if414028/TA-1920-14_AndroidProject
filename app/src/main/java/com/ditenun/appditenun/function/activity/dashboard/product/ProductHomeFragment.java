package com.ditenun.appditenun.function.activity.dashboard.product;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ItemCategoryBinding;
import com.ditenun.appditenun.databinding.ItemNewArrivalsBinding;
import com.ditenun.appditenun.databinding.ProductHomeFragmentBinding;
import com.ditenun.appditenun.dependency.models.Category;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.activity.cart.CartActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;

import java.util.ArrayList;

public class ProductHomeFragment extends Fragment {

    private ProductHomeViewModel mViewModel;
    private ProductHomeFragmentBinding binding;

    private SimpleRecyclerAdapter<Product> newArrivalsAdapter;
    private SimpleRecyclerAdapter<Category> categoryAdapter;

    public static ProductHomeFragment newInstance() {
        return new ProductHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductHomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.product_home_fragment, container, false);

        initLayout();

        return binding.getRoot();
    }

    private void initLayout() {
        initImageSlider();
        initNewArrivalsRecyclerView();
        initCategoryRecyclerView();
    }

    private void initImageSlider() {
        TextSliderView textSliderView1 = new TextSliderView(getContext());
        textSliderView1.description("image 1").image(R.drawable.img_dashboard);
        binding.imageSlider.addSlider(textSliderView1);

        TextSliderView textSliderView2 = new TextSliderView(getContext());
        textSliderView2.description("image 2").image(R.drawable.img_dashboard);
        binding.imageSlider.addSlider(textSliderView2);

        TextSliderView textSliderView3 = new TextSliderView(getContext());
        textSliderView3.description("image 3").image(R.drawable.img_dashboard);
        binding.imageSlider.addSlider(textSliderView3);
    }

    private void initNewArrivalsRecyclerView() {
        binding.rvNewArrivals.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        newArrivalsAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_new_arrivals, (holder, item) -> {
            ItemNewArrivalsBinding itemBinding = (ItemNewArrivalsBinding) holder.getLayoutBinding();
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DetailProductActivity.class);
                    startActivity(intent);
                }
            });
        });
        binding.rvNewArrivals.setAdapter(newArrivalsAdapter);
        newArrivalsAdapter.setMainData(mViewModel.getNewArrivalsProductList());
    }

    private void initCategoryRecyclerView() {
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        categoryAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_category, (holder, item) -> {
            ItemCategoryBinding itemBinding = (ItemCategoryBinding) holder.getLayoutBinding();
        });
        binding.rvCategory.setAdapter(categoryAdapter);
        categoryAdapter.setMainData(mViewModel.getCategoryList());
    }

}