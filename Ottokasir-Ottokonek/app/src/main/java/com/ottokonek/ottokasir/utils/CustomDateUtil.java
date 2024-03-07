//package id.ottopay.kasir.utils;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.whiteelephant.monthpicker.MonthPickerDialog;
//
//import java.util.Calendar;
//
//public class CustomDateUtil {
//
//    public static void showMonthYearPicker(Context context) {
//        Calendar today = Calendar.getInstance();
//
//
//        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(context,
//                new MonthPickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(int selectedMonth, int selectedYear) { // on date set }
//                    }
//                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
//
//        builder.setActivatedMonth(Calendar.JULY)
//                .setMinYear(1990)
//                .setActivatedYear(2017)
//                .setMaxYear(2030)
//                .setMinMonth(Calendar.FEBRUARY)
//                .setTitle("Select trading month")
//                .setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
//                // .setMaxMonth(Calendar.OCTOBER)
//                // .setYearRange(1890, 1890)
//                // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
//                //.showMonthOnly()
//                // .showYearOnly()
//                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
//                    @Override
//                    public void onMonthChanged(int selectedMonth) {
//                        Log.d("", "Selected month : " + selectedMonth);
////                        Toast.makeText(context, " Selected month : " + selectedMonth, Toast.LENGTH_SHORT).show();
//                    }
//
//
//                })
//                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
//                    @Override
//                    public void onYearChanged(int selectedYear) {
//                        Log.d("", "Selected year : " + selectedYear);
////                         Toast.makeText(context, " Selected year : " + selectedYear, Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .build()
//                .show();
//
//
//    }
//
////    @SuppressLint("NewApi")
////    public static DatePickerDialog showMonthYearPickerx(Context context) {
////        DatePickerDialog dpd = new DatePickerDialog(context);
////        try {
////
////            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
////            for (Field datePickerDialogField : datePickerDialogFields) {
////                if (datePickerDialogField.getName().equals("mDatePicker")) {
////                    datePickerDialogField.setAccessible(true);
////                    DatePicker datePicker = (DatePicker) datePickerDialogField
////                            .get(dpd);
////                    Field datePickerFields[] = datePickerDialogField.getType()
////                            .getDeclaredFields();
////                    for (Field datePickerField : datePickerFields) {
////                        if ("mDayPicker".equals(datePickerField.getName())
////                                || "mDaySpinner".equals(datePickerField
////                                .getName())) {
////                            datePickerField.setAccessible(true);
////                            Object dayPicker = new Object();
////                            dayPicker = datePickerField.get(datePicker);
////                            ((View) dayPicker).setVisibility(View.GONE);
////                        }
////                    }
////                }
////
////            }
////        } catch (Exception ex) {
////        }
////        return dpd;
////    }
//
//}
