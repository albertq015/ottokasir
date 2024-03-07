package com.ottokonek.ottokasir.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import com.ottokonek.ottokasir.App;


public class DeviceUtil extends app.beelabs.com.codebase.support.util.DeviceUtil {


    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = App.Companion.getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = App.Companion.getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return manager.getDeviceId();
        } else {
            //SessionManager.logout();
            Log.e("Get IMEI", "Tidak ada ijin untuk membuat id device");
            //Toast.makeText(cuntext, “Tidak ada ijin untuk membuat id device”, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);

            return UUID.randomUUID().toString();
        }
    }

    public static String getTodayFormattedDate(String format, int dayDeviation, int monthDeviation) {
        SimpleDateFormat formatter;
        Calendar now = Calendar.getInstance();
        try {
            formatter = new SimpleDateFormat(format);
            now.set(now.get(Calendar.YEAR),
                    (now.get(Calendar.MONTH) + 1) - monthDeviation,
                    now.get(Calendar.DAY_OF_MONTH) - dayDeviation);
            return formatter.format(now.getTime());
        } catch (Exception e) {
            return "invalid date format";
        }
    }

    /**
     * @param input put all numeric
     * @return valid digits of eas13 barcode
     */
    public static String generateEas13(String input) {
        int sum = 0;
        int e = 0;
        int o = 0;
        for (int i = 0; i < input.length(); i++) {
            if ((int) input.charAt(i) % 2 == 0) {
                e += (int) input.charAt(i);
            } else {
                o += (int) input.charAt(i);
            }
        }
        o = o * 3;
        int total = o + e;
        if (total % 10 == 0) {
            sum = 0;//ceksum 0
        } else {
            sum = 10 - (total % 10);
        }
        return input + sum;
    }


    /**
     * Check exceptional device
     *
     * @return exceptional device status
     */
    public static boolean isExceptionalDevice() {
        String[] exceptionalDevices = {"xiaomi", "meizu"};
        String manufacturer = Build.MANUFACTURER;

        boolean findMatchData = Arrays.asList(exceptionalDevices).contains(manufacturer.toLowerCase());

        return findMatchData;
    }


}
