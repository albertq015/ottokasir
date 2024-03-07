package com.ottokonek.ottokasir.ui.callback;

import app.beelabs.com.codebase.base.contract.IView;

public interface ViewBaseInterface extends IView {

    void onApiFailed(String title, String msg);

    void onConnectionFailed(String msg);

    void handleError(String message);

    void showLoading();

    void hideLoading();

    void logout();

    void showPinCheckerView();

    void showToast(String msg);

    void killActivity();
}
