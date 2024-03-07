package com.ottokonek.ottokasir.ui.dialog.snack_bar;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import com.ottokonek.ottokasir.R;

public class TopSnackbar {

    public static void showSnackBarBlue(Context context, View view, String message) {
        try {
            TSnackbar snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();

            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSnackBarRed(Context context, View view, String message) {
        try {
            TSnackbar snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();

            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
