package com.ottokonek.ottokasir.ui.fragment;

import android.content.Intent;
import android.widget.Toast;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseFragment;
import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.presenter.manager.SessionManager;
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface;
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity;
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog;

public class BaseLocalFragment extends BaseFragment implements ViewBaseInterface {

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onApiFailed(String title, String error) {
        hideLoading();


        if (error.contains("Failed to connect to ") || error.contains("Unable to resolve host")) {
            Toast.makeText(requireActivity(), getString(R.string.tidak_ada_koneksi), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show();
        }

        /*ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getActivity()
                .findViewById(android.R.id.content)).getChildAt(0);
        Snackbar snackbar = Snackbar
                .make(viewGroup, error, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
        TextView stv = (snackbar.getView()).findViewById(R.id.snackbar_text);
        stv.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        snackbar.show();*/
    }

    @Override
    public void onConnectionFailed(String msg) {
        hideLoading();
        if (msg.contains("Failed to connect to ") || msg.contains("Unable to resolve host")) {
            Toast.makeText(requireActivity(), getString(R.string.tidak_ada_koneksi), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), getString(R.string.tidak_ada_koneksi), Toast.LENGTH_SHORT).show();
        }
        //onApiFailed("Connection Lost", msg);
    }

    @Override
    public void showLoading() {
        CustomProgressDialog.Companion.showDialog(getBaseActivity(), "Loading");
    }

    @Override
    public void hideLoading() {
        CustomProgressDialog.Companion.closeDialog();
    }

    @Override
    public void handleError(String message) {
        hideLoading();
        if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
            Toast.makeText(requireActivity(), getString(R.string.tidak_ada_koneksi), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void logout() {
        hideLoading();

        SessionManager.clearSessionLogin(getActivity());
        Intent x = new Intent(getActivity(), LoginActivity.class);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(x);
    }

    @Override
    public void showPinCheckerView() {

    }

    @Override
    public void showToast(String msg) {
        getBaseActivity().finish();
    }

    @Override
    public void killActivity() {

    }

    @Override
    public BaseActivity getCurrentActivity() {
        return null;
    }
}
