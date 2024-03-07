package com.ottokonek.ottokasir.presenter.manager;

import android.content.Context;
import android.content.Intent;

import app.beelabs.com.codebase.support.util.CacheUtil;
import com.ottokonek.ottokasir.IConfig;
import com.ottokonek.ottokasir.dao.cart.CartManager;
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity;

public class SessionManager {

    public static void putSessionLogin(boolean isLogin, Context context) {
        CacheUtil.putPreferenceBoolean(IConfig.Companion.getSESSION_LOGIN_KEY(), isLogin, context);
    }

    public static void clearSessionLogin(Context context) {
        CacheUtil.putPreferenceBoolean(IConfig.Companion.getSESSION_LOGIN_KEY(), false, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_TOKEN_CREDENTIAL(), "", context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_PIN(), "", context);

        CartManager.removeAllCartItem();
    }

    public static boolean isLogin(Context context) {
        return CacheUtil.getPreferenceBoolean(IConfig.Companion.getSESSION_LOGIN_KEY(), context);
    }

    public static void putCredential(int userId, String token, String pin, String email, String phone, String name,
                                     String merchantName, String businessType, String avatar, String merchantID, String address, String province, int storeTypeId, String storeTypeName, String nmId, String mid, String mpan, Context context) {

        CacheUtil.putPreferenceInteger(IConfig.Companion.getSESSION_USER_ID(), userId, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_TOKEN_CREDENTIAL(), token, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_PIN(), pin, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_EMAIL(), email, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_PHONE(), phone, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_NAME(), name, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_MERCHANT_NAME(), merchantName, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_BUSINESS_TYPE(), businessType, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_IMAGE(), avatar, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_MERCHANT_ID(), merchantID, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_ADDRESS(), address, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_PROVINCE(), province, context);
        CacheUtil.putPreferenceInteger(IConfig.Companion.getSESSION_STORE_TYPE_ID(), storeTypeId, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_STORE_TYPE_NAME(), storeTypeName, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_MPAN(), mpan, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_MID(), mid, context);
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_NMID(), nmId, context);
    }

    public static void putStoreTypeId(int storeTypeId, Context context) {
        CacheUtil.putPreferenceInteger(IConfig.Companion.getSESSION_STORE_TYPE_ID(), storeTypeId, context);
    }

    public static int getStoreTypeId(Context context) {
        return CacheUtil.getPreferenceInteger(IConfig.Companion.getSESSION_STORE_TYPE_ID(), context);
    }

    public static void putStoreTypeName(String storeTypeName, Context context) {
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_STORE_TYPE_NAME(), storeTypeName, context);
    }

    public static String getStoreTypeName(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_STORE_TYPE_NAME(), context);
    }

    public static void putName(String name, Context context) {
        CacheUtil.putPreferenceString(IConfig.Companion.getSESSION_NAME(), name, context);
    }

    public static String getName(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_NAME(), context);
    }

    public static void setFirstLogin(Context context) {
        CacheUtil.putPreferenceBoolean(IConfig.Companion.getNO_FIRST_ENTER(), true, context);
    }

    public static String getCredential(Context context) {
        String token = CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_TOKEN_CREDENTIAL(), context);
        return token != null ? token : "";
    }

    public static String getEmail(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_EMAIL(), context);
    }

    public static String getPhone(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_PHONE(), context);
    }

    public static String getPIN(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_PIN(), context);
    }

    public static String getAddress(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_ADDRESS(), context);
    }

    public static String getMerchantId(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_MERCHANT_ID(), context);

    }

    public static String getMerchantName(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_MERCHANT_NAME(), context);

    }

    public static String getUsername(Context context) {
        return CacheUtil.getPreferenceString(IConfig.Companion.getSESSION_NAME(), context);
    }

    public static Boolean isFirstLogin(Context context) {
        return CacheUtil.getPreferenceBoolean(IConfig.Companion.getNO_FIRST_ENTER(), context);

    }


    public static void putStocks(int stocks, Context context) {
        CacheUtil.putPreferenceInteger(IConfig.Companion.getKEY_STOCKS(), stocks, context);
    }

    public static int getStocks(Context context) {
        return CacheUtil.getPreferenceInteger(IConfig.Companion.getKEY_STOCKS(), context);
    }


    public static void putStocksAlert(int stocksAlert, Context context) {
        CacheUtil.putPreferenceInteger(IConfig.Companion.getKEY_STOCKS_ALERT(), stocksAlert, context);
    }

    public static int getStocksAlert(Context context) {
        return CacheUtil.getPreferenceInteger(IConfig.Companion.getKEY_STOCKS_ALERT(), context);
    }

    public static void putIsStockActive(boolean isStockActive, Context context) {
        CacheUtil.putPreferenceBoolean(IConfig.Companion.getKEY_IS_STOCK_ACTIVE(), isStockActive, context);
    }

    public static Boolean getIsStockActive(Context context) {
        return CacheUtil.getPreferenceBoolean(IConfig.Companion.getKEY_IS_STOCK_ACTIVE(), context);
    }


    public static void putIsNotFirstTimeOnboardingKasbon(boolean isNotFirstTime, Context context) {
        CacheUtil.putPreferenceBoolean(IConfig.Companion.getKEY_FIRST_TIME_ONBOARDING_KASBON(), isNotFirstTime, context);
    }

    public static Boolean getIsNotFirstTimeOnboardingKasbon(Context context) {
        return CacheUtil.getPreferenceBoolean(IConfig.Companion.getKEY_FIRST_TIME_ONBOARDING_KASBON(), context);
    }

    public static void putIsNotFirstTimeOnboardingManageProduct(boolean isNotFirstTime, Context context) {
        CacheUtil.putPreferenceBoolean(IConfig.Companion.getKEY_FIRST_TIME_ONBOARDING_MANAGE_PRODUCT(), isNotFirstTime, context);
    }

    public static Boolean getIsNotFirstTimeOnboardingManageProduct(Context context) {
        return CacheUtil.getPreferenceBoolean(IConfig.Companion.getKEY_FIRST_TIME_ONBOARDING_MANAGE_PRODUCT(), context);
    }


    public static void logoutDevice(Context context) {
        clearSessionLogin(context);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
