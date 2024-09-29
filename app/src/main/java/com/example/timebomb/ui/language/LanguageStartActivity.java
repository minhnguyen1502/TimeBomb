package com.example.timebomb.ui.language;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityLanguageStartBinding;
import com.example.timebomb.ui.intro.IntroActivity;
import com.example.timebomb.ui.language.adapter.LanguageStartAdapter;
import com.example.timebomb.ui.language.model.LanguageModel;
import com.example.timebomb.util.SystemUtil;
import com.example.timebomb.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageStartActivity extends BaseActivity<ActivityLanguageStartBinding> {

    List<LanguageModel> listLanguage;
    String codeLang = "";

    @Override
    public ActivityLanguageStartBinding getBinding() {
        return ActivityLanguageStartBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageStartAdapter = new LanguageStartAdapter(listLanguage, code -> codeLang = code, this);

        binding.rcvLangStart.setLayoutManager(linearLayoutManager);
        binding.rcvLangStart.setAdapter(languageStartAdapter);
    }

    @Override
    public void bindView() {
        binding.ivCheck.setOnClickListener(view -> {

            if (codeLang.isEmpty()) {
                Toast.makeText(LanguageStartActivity.this, "please choose your language", Toast.LENGTH_SHORT).show();
            } else {
                SystemUtil.saveLocale(getBaseContext(), codeLang);
                Utils.setLanguageSelected(true);
                startNextActivity(IntroActivity.class, null);
                finishAffinity();
            }
        });

    }

    @Override
    public void onBack() {
        finishAffinity();
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        listLanguage.add(new LanguageModel("English", "en"));
        listLanguage.add(new LanguageModel("Portuguese", "pt"));
        listLanguage.add(new LanguageModel("Spanish", "es"));
        listLanguage.add(new LanguageModel("German", "de"));
        listLanguage.add(new LanguageModel("French", "fr"));
        listLanguage.add(new LanguageModel("China", "zh"));
        listLanguage.add(new LanguageModel("Hindi", "hi"));
        listLanguage.add(new LanguageModel("Indonesia", "in"));

    }
}