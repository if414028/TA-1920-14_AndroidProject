package com.ditenun.appditenun.function.activity.commerce.dashboard.product;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
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

import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ItemCategoryBinding;
import com.ditenun.appditenun.databinding.ItemNewArrivalsBinding;
import com.ditenun.appditenun.databinding.ProductHomeFragmentBinding;
import com.ditenun.appditenun.dependency.models.Category;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.activity.commerce.catalogue.DetailProductActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

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
        observeLiveEvent();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.clearProductList();
        mViewModel.fetchAllProduct();
    }

    private void initLayout() {
        initImageSlider();
        initNewArrivalsRecyclerView();
//        initCategoryRecyclerView();
    }

    private void initImageSlider() {
        TextSliderView textSliderView1 = new TextSliderView(getContext());
        textSliderView1.image("https://firebasestorage.googleapis.com/v0/b/ditenun-62c37.appspot.com/o/product%2Fimage%2F1a.png?alt=media&token=3f915c43-6e85-4ca4-a6ae-57fe90ca0de0");
        binding.imageSlider.addSlider(textSliderView1);

        TextSliderView textSliderView2 = new TextSliderView(getContext());
        textSliderView2.image("https://firebasestorage.googleapis.com/v0/b/ditenun-62c37.appspot.com/o/product%2Fimage%2F2a.png?alt=media&token=626b9922-72b8-45eb-be9b-4728cdef24c7");
        binding.imageSlider.addSlider(textSliderView2);

        TextSliderView textSliderView3 = new TextSliderView(getContext());
        textSliderView3.image("https://firebasestorage.googleapis.com/v0/b/ditenun-62c37.appspot.com/o/product%2Fimage%2F3a.png?alt=media&token=0df48efb-8657-4a44-9392-8f3f33290bcb");
        binding.imageSlider.addSlider(textSliderView3);
    }

    private void initNewArrivalsRecyclerView() {
        binding.rvNewArrivals.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        newArrivalsAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_new_arrivals, (holder, item) -> {
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
        binding.rvNewArrivals.setAdapter(newArrivalsAdapter);
    }

//    private void initCategoryRecyclerView() {
//        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//        categoryAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_category, (holder, item) -> {
//            ItemCategoryBinding itemBinding = (ItemCategoryBinding) holder.getLayoutBinding();
//            Picasso.with(getContext()).load(item.getCategoryImage()).into(itemBinding.ivCategory);
//        });
//        binding.rvCategory.setAdapter(categoryAdapter);
//        categoryAdapter.setMainData(mViewModel.getCategoryList());
//    }

    private void observeLiveEvent() {
        mViewModel.getSuccessGetListProductEvent().observe(this, aVoid -> {
            newArrivalsAdapter.setMainData(mViewModel.getNewArrivalsProductList());
            newArrivalsAdapter.notifyDataSetChanged();
            binding.progressBar.setVisibility(View.GONE);
        });
        mViewModel.getErrorGetListProductEvent().observe(this, databaseError -> {
            binding.progressBar.setVisibility(View.GONE);
        });
    }
}