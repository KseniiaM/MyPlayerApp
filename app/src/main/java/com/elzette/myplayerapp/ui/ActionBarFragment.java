package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elzette.myplayerapp.databinding.ActionBarFragmentBinding;
import com.elzette.myplayerapp.viewModels.ActionBarViewModel;

public class ActionBarFragment extends Fragment {

    ActionBarFragmentBinding binding;
    ActionBarViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InitBinding(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void InitBinding(LayoutInflater inflater) {
        viewModel = ViewModelProviders.of(this)
                                      .get(ActionBarViewModel.class);
        binding = ActionBarFragmentBinding.inflate(inflater);
        binding.setViewModel(viewModel);
        binding.setActionBarFragment(this);
        binding.setLifecycleOwner(this);
    }
}
