package com.ottokonek.ottokasir.ui.component;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MyValueFormatter extends ValueFormatter
{

    private final DecimalFormat mFormat;

    public MyValueFormatter(String format) {
        if (format.equals("value")) {
            Locale locale = new Locale("en","PH");
            NumberFormat formatRupiah=NumberFormat.getCurrencyInstance(locale);
            mFormat = (DecimalFormat) formatRupiah;
        }else{
            mFormat = new DecimalFormat("#");
            mFormat.setDecimalSeparatorAlwaysShown(false);
        }
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) ;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            return mFormat.format(value);
        } else if (value > 0) {
            return mFormat.format(value) ;
        } else {
            return mFormat.format(value);
        }
    }
}
