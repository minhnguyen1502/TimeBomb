package com.example.timebomb.dialog.rate;


import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseDialog;
import com.example.timebomb.databinding.DialogRatingAppBinding;
import com.example.timebomb.util.EventTracking;


public class RatingDialog extends BaseDialog<DialogRatingAppBinding> {

    private IClickDialogRate iClickDialogRate;
    private final Context context;

    public RatingDialog(@NonNull Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }

    @Override
    protected DialogRatingAppBinding setBinding() {
        return DialogRatingAppBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.rtb.setRating(5.0f);

        binding.rtb.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            String getRating = String.valueOf(binding.rtb.getRating());
            switch (getRating) {
                case "1.0":
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_1);
                    break;
                case "2.0":
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_2);
                    break;
                case "3.0":
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_3);
                    break;
                case "4.0":
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_4);
                    break;
                case "5.0":
                    binding.btnRateUs.setText(context.getResources().getString(R.string.thank_you));
                    binding.imgIcon.setImageResource(R.drawable.rating_5);
                    break;
                default:
                    binding.btnRateUs.setText(context.getResources().getString(R.string.rate_us));
                    binding.imgIcon.setImageResource(R.drawable.rating_0);
                    break;
            }
        });
    }

    @Override
    protected void bindView() {
        binding.btnRateUs.setOnClickListener(view -> {
            if (binding.rtb.getRating() == 0) {
                return;
            }
            if (binding.rtb.getRating() <= 3.0) {
                binding.imgIcon.setVisibility(View.GONE);
                iClickDialogRate.send();
            } else {
                binding.imgIcon.setVisibility(View.VISIBLE);
                iClickDialogRate.rate();
            }
            EventTracking.logEvent(context, "rate_submit", "rate_star - ", "" + binding.rtb.getRating());

        });

        binding.btnNotNow.setOnClickListener(view -> {
            EventTracking.logEvent(context, "rate_not_now");
            iClickDialogRate.later();
        });
    }

    public void init(IClickDialogRate iClickDialogRate) {
        this.iClickDialogRate = iClickDialogRate;
    }

    public String getRating() {
        return String.valueOf(this.binding.rtb.getRating());
    }

}
