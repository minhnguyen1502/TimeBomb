package com.example.timebomb.ui.policy;

import android.annotation.SuppressLint;
import android.view.View;

import com.example.timebomb.R;
import com.example.timebomb.ads.IsNetWork;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityPolicyBinding;

public class PolicyActivity extends BaseActivity<ActivityPolicyBinding> {

    String linkPolicy = "";

    @Override
    public ActivityPolicyBinding getBinding() {
        return ActivityPolicyBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(linkPolicy);
        if (IsNetWork.haveNetworkPolicy(this)) {
            binding.webView.setVisibility(View.VISIBLE);
            binding.ivNoInternet.setVisibility(View.GONE);
            binding.tvNoInternet.setVisibility(View.GONE);

            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.loadUrl(linkPolicy);
        } else {
            binding.webView.setVisibility(View.GONE);
            binding.ivNoInternet.setVisibility(View.VISIBLE);
            binding.tvNoInternet.setVisibility(View.VISIBLE);
        }

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(linkPolicy);
        binding.ivBack.setOnClickListener(v -> onBack());
    }

    @Override
    public void bindView() {
    }

    @Override
    public void onBack() {
        finishThisActivity();
    }
}
