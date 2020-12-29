package com.ditenun.appditenun.function.activity.commerce.catalogue;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.FragmentProductDescriptionBinding;
import com.ditenun.appditenun.dependency.models.Product;

public class ProductDescriptionFragment extends DialogFragment {

    private DetailProductViewModel viewModel;
    private FragmentProductDescriptionBinding binding;

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.filterDialogTheme);
        viewModel = ViewModelProviders.of(getActivity()).get(DetailProductViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_description, container, false);

        initLayout();

        return binding.getRoot();
    }

    private void initLayout() {
        binding.tvProductDescription.setText(viewModel.getProduct().getDescription());
        binding.btnBack.setOnClickListener(view -> dismiss());
    }
}
