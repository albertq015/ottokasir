package com.ottokonek.ottokasir.ui.dialog;

import android.app.Activity;
import androidx.annotation.NonNull;

import app.beelabs.com.codebase.base.BaseDialog;

public class BluetoothPrinterSelectDialog extends BaseDialog {
    private Activity activity;

    public BluetoothPrinterSelectDialog(@NonNull Activity activity, int style) {
        super(activity, style);
        this.activity = activity;
    }


}
