package com.ottokonek.ottokasir.ui.dialog;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import app.beelabs.com.codebase.base.BasePresenter;
import app.beelabs.com.codebase.base.contract.IView;
import butterknife.OnClick;
import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.presenter.AuthPresenter;

public class ExitAppLogoutDialog extends ExitAppDialog {
    private Activity activity;
    private AuthPresenter presenter;

    public ExitAppLogoutDialog(@NonNull Activity activity, int style) {
        super(activity, style);
        this.activity = activity;
        presenter=(AuthPresenter) BasePresenter.getInstance((IView)activity ,AuthPresenter.class);
    }

    @OnClick({R.id.cancelButton, R.id.logoutAction})
    public void onAction(View view) {
        if (view.getId() == R.id.logoutAction) {
            presenter.doLogout(activity);
            dismiss();
        } else {
            dismiss();
        }
    }
}
