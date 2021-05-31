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
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ItemCategoryBinding;
import com.ditenun.appditenun.databinding.ItemNewArrivalsBinding;
import com.ditenun.appditenun.databinding.ProductHomeFragmentBinding;
import com.ditenun.appditenun.dependency.App;
import com.ditenun.appditenun.dependency.models.Category;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.network.TenunNetworkInterface;
import com.ditenun.appditenun.function.activity.commerce.catalogue.DetailProductActivity;
import com.ditenun.appditenun.function.util.SimpleRecyclerAdapter;
import com.ditenun.appditenun.function.util.TextUtil;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

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
        mViewModel.fetchListCategories();
    }

    private void initLayout() {
        initImageSlider();
        initNewArrivalsRecyclerView();
        initCategoryRecyclerView();
    }

    private void initImageSlider() {
        TextSliderView textSliderView1 = new TextSliderView(getContext());
        textSliderView1.image("https://firebasestorage.googleapis.com/v0/b/ditenun-62c37.appspot.com/o/product%2Fimage%2FHero%20banner%201.png?alt=media&token=404bcdc1-cd0b-496b-92ce-16ef1ec96d57");
        binding.imageSlider.addSlider(textSliderView1);

        TextSliderView textSliderView2 = new TextSliderView(getContext());
        textSliderView2.image("https://firebasestorage.googleapis.com/v0/b/ditenun-62c37.appspot.com/o/product%2Fimage%2FHero%20banner%202.png?alt=media&token=ed1595f5-84d5-4ea6-9855-5bc783625c6c");
        binding.imageSlider.addSlider(textSliderView2);

        TextSliderView textSliderView3 = new TextSliderView(getContext());
        textSliderView3.image("https://firebasestorage.googleapis.com/v0/b/ditenun-62c37.appspot.com/o/product%2Fimage%2FHero%20banner%203.png?alt=media&token=1b838228-25a0-40e3-8431-ed5c88e00c21");
        binding.imageSlider.addSlider(textSliderView3);
    }

    private void initNewArrivalsRecyclerView() {
        binding.rvNewArrivals.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        newArrivalsAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_new_arrivals, (holder, item) -> {
            ItemNewArrivalsBinding itemBinding = (ItemNewArrivalsBinding) holder.getLayoutBinding();
            if (item != null) {
                if (item.getImages() != null) {
                    Picasso.with(getContext()).load(item.getImages().get(0).getSrc()).into(itemBinding.imgNewArrivals);
                }
            }
            itemBinding.tvProductName.setText(item.getName());
            itemBinding.tvProductPrice.setText(TextUtil.getInstance().formatToRp(item.getPriceInDouble()));
            itemBinding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DetailProductActivity.class);
                intent.putExtra("product_id", item.getId());
                intent.putExtra("product", item);
                startActivity(intent);
            });
        });
        binding.rvNewArrivals.setAdapter(newArrivalsAdapter);
    }

    private void initCategoryRecyclerView() {
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        categoryAdapter = new SimpleRecyclerAdapter<>(new ArrayList<>(), R.layout.item_category, (holder, item) -> {
            ItemCategoryBinding itemBinding = (ItemCategoryBinding) holder.getLayoutBinding();
            if (item.getCategoryImage() != null) {
                Picasso.with(getContext()).load(item.getCategoryImage()).into(itemBinding.ivCategory);
            } else {
                itemBinding.ivCategory.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_broken_image_24));
            }
            itemBinding.tvCategoryName.setText(item.getCategoryName());
        });
        binding.rvCategory.setAdapter(categoryAdapter);
    }

    private void observeLiveEvent() {
        mViewModel.getSuccessGetListProductEvent().observe(this, aVoid -> {
            newArrivalsAdapter.setMainData(mViewModel.getNewArrivalsProductList());
            newArrivalsAdapter.notifyDataSetChanged();
            binding.progressBar.setVisibility(View.GONE);
        });
        mViewModel.getErrorGetListProductEvent().observe(this, databaseError -> {
            binding.progressBar.setVisibility(View.GONE);
        });

        mViewModel.getSuccessGetListCategories().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                categoryAdapter.setMainData(mViewModel.getCategoryList());
                categoryAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.getErrorGetListCategories().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_LONG).show();
            }
        });
    }
}