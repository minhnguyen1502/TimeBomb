package com.example.timebomb.ui.language;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityLanguageBinding;
import com.example.timebomb.ui.language.adapter.LanguageAdapter;
import com.example.timebomb.ui.language.model.LanguageModel;
import com.example.timebomb.ui.main.MainActivity;
import com.example.timebomb.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {

    List<LanguageModel> listLanguage;
    String codeLang;

    @Override
    public ActivityLanguageBinding getBinding() {
        return ActivityLanguageBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        codeLang = Locale.getDefault().getLanguage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageAdapter languageAdapter = new LanguageAdapter(listLanguage, code -> codeLang = code, this);
        languageAdapter.setCheck(SystemUtil.getPreLanguage(getBaseContext()));

        binding.rcvLang.setLayoutManager(linearLayoutManager);
        binding.rcvLang.setAdapter(languageAdapter);
        binding.ivChoose.setOnClickListener(view -> {
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            startActivity(new Intent(LanguageActivity.this, MainActivity.class));
            finishAffinity();
        });
        binding.ivBack.setOnClickListener(v -> onBack());
    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        finish();
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        listLanguage.add(new LanguageModel("English", "en"));
        listLanguage.add(new LanguageModel("China", "zh"));
        listLanguage.add(new LanguageModel("French", "fr"));
        listLanguage.add(new LanguageModel("German", "de"));
        listLanguage.add(new LanguageModel("Hindi", "hi"));
        listLanguage.add(new LanguageModel("Indonesia", "in"));
        listLanguage.add(new LanguageModel("Portuguese", "pt"));
        listLanguage.add(new LanguageModel("Spanish", "es"));
    }

}
