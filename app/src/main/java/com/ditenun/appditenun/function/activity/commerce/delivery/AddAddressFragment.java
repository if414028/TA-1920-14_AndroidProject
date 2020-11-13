package com.ditenun.appditenun.function.activity.commerce.delivery;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.FragmentAddAddressBinding;

public class AddAddressFragment extends DialogFragment {

    private FragmentAddAddressBinding binding;
    private DeliveryViewModel viewModel;

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
        viewModel = ViewModelProviders.of(getActivity()).get(DeliveryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false);

        initLayout();

        return binding.getRoot();
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(view -> dismiss());
        binding.etAddress.setText(viewModel.getAddress());
        binding.btnSubmit.setOnClickListener(view -> {
            String address = binding.etAddress.getText().toString();
            viewModel.submitAddress(address);
            dismiss();
        });
    }
}
