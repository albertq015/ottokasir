package com.ottokonek.ottokasir.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import app.beelabs.com.codebase.base.BaseDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ottokonek.ottokasir.R;

public class ConfirmationEmailDialog extends BaseDialog {

    @BindView(R.id.ivClose)
    ImageView ivClose;
    private Activity activity;

    public ConfirmationEmailDialog(@NonNull Activity activity, int style) {
        super(activity, style);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowContentDialogLayout(R.layout.dialog_confirmation_email);

        ButterKnife.bind(this);

        ivClose.setOnClickListener(view->{
            this.dismiss();
        });
    }



}
