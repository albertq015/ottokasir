package com.ottokonek.ottokasir.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import app.beelabs.com.codebase.base.BaseDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ottokonek.ottokasir.R;

public class ExitAppDialog extends BaseDialog {
    private Activity activity;
    @BindView(R.id.tv_infotext)
    TextView tvInfotext;

    String msg;

    public ExitAppDialog(@NonNull Activity activity, int style) {
        super(activity, style);
        this.activity = activity;
    }

    public ExitAppDialog(@NonNull Activity activity, int style, String msg) {
        super(activity, style);
        this.activity = activity;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowContentDialogLayout(R.layout.dialog_exit_app);
        this.getWindow().setBackgroundDrawableResource(R.color.transaparent_black);
        ButterKnife.bind(this);
        if (msg != null)
            tvInfotext.setText(msg);
    }

    @OnClick({R.id.cancelButton, R.id.logoutAction})
    public void onAction(View view) {
        if (view.getId() == R.id.logoutAction) {
//            ActionUtil.Companion.logoutAction(activity);
            activity.finish();
        } else {
            dismiss();
        }
    }


}
