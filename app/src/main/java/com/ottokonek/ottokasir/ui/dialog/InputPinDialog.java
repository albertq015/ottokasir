package com.ottokonek.ottokasir.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import app.beelabs.com.codebase.base.BaseDialog;
import app.beelabs.com.codebase.support.util.CacheUtil;
import com.ottokonek.ottokasir.IConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ottokonek.ottokasir.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InputPinDialog extends BaseDialog {
    @BindView(R.id.pin1)
    ImageView pin1;
    @BindView(R.id.pin2)
    ImageView pin2;
    @BindView(R.id.pin3)
    ImageView pin3;
    @BindView(R.id.pin4)
    ImageView pin4;
    @BindView(R.id.pin5)
    ImageView pin5;
    @BindView(R.id.pin6)
    ImageView pin6;

    private Disposable disposable;

    private int digitIndex = 0;
    private int[] digits = new int[]{-1, -1, -1, -1, -1, -1};

    private Activity activity;

    public InputPinDialog(@NonNull Activity activity, int style) {
        super(activity, style);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_pin);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button_back})
    public void onPressKeypad(View view) {
        if (digitIndex > 5 && view.getId() != R.id.button_back) return;

        if (view.getId() != R.id.button_back) {
            if (digitIndex == 0) pin1.setImageResource(R.drawable.circle_yellow_solid);
            if (digitIndex == 1) pin2.setImageResource(R.drawable.circle_yellow_solid);
            if (digitIndex == 2) pin3.setImageResource(R.drawable.circle_yellow_solid);
            if (digitIndex == 3) pin4.setImageResource(R.drawable.circle_yellow_solid);
            if (digitIndex == 4) pin5.setImageResource(R.drawable.circle_yellow_solid);
            if (digitIndex == 5) pin6.setImageResource(R.drawable.circle_yellow_solid);
        }

        switch (view.getId()) {
            case R.id.button1:
                digits[digitIndex++] = 1;
                break;

            case R.id.button2:
                digits[digitIndex++] = 2;
                break;

            case R.id.button3:
                digits[digitIndex++] = 3;
                break;

            case R.id.button4:
                digits[digitIndex++] = 4;
                break;

            case R.id.button5:
                digits[digitIndex++] = 5;
                break;

            case R.id.button6:
                digits[digitIndex++] = 6;
                break;

            case R.id.button7:
                digits[digitIndex++] = 7;
                break;

            case R.id.button8:
                digits[digitIndex++] = 8;
                break;

            case R.id.button9:
                digits[digitIndex++] = 9;
                break;

            case R.id.button0:
                digits[digitIndex++] = 0;
                break;

            case R.id.button_back:
                if (digitIndex <= 0) return;

                digits[--digitIndex] = -1;
                if (digitIndex == 0) pin1.setImageResource(R.drawable.circle_greyishbrown_solid);
                if (digitIndex == 1) pin2.setImageResource(R.drawable.circle_greyishbrown_solid);
                if (digitIndex == 2) pin3.setImageResource(R.drawable.circle_greyishbrown_solid);
                if (digitIndex == 3) pin4.setImageResource(R.drawable.circle_greyishbrown_solid);
                if (digitIndex == 4) pin5.setImageResource(R.drawable.circle_greyishbrown_solid);
                if (digitIndex == 5) pin6.setImageResource(R.drawable.circle_greyishbrown_solid);

                break;
        }

        if (digitIndex == 6) {
            String digitNumber = getNumberFromArrayDigit(digits);

            String pin = CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_PIN(), activity);
            if (digitNumber.equals(pin)) {
                dismiss();
            } else {
                Toast.makeText(activity, "Kode Pin Salah", Toast.LENGTH_SHORT).show();


                disposable = Observable.timer(500, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .repeat()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                clearDigit();
                                disposable.dispose();
                            }

                        });

            }


        }
    }

    private void clearDigit() {
        digitIndex = 0;
        pin1.setImageResource(R.drawable.circle_greyishbrown_solid);
        pin2.setImageResource(R.drawable.circle_greyishbrown_solid);
        pin3.setImageResource(R.drawable.circle_greyishbrown_solid);
        pin4.setImageResource(R.drawable.circle_greyishbrown_solid);
        pin5.setImageResource(R.drawable.circle_greyishbrown_solid);
        pin6.setImageResource(R.drawable.circle_greyishbrown_solid);
    }

    private String getNumberFromArrayDigit(int[] digits) {
        String nums = "";
        for (int d : digits) {
            if (d > -1) nums += d;

        }
        return nums;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
