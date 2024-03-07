package com.ottokonek.ottokasir.dao.cart;

import com.ottokonek.ottokasir.dao.manager.LocalDbManager;
import com.ottokonek.ottokasir.model.miscModel.ProductItemModel;
import com.ottokonek.ottokasir.model.db.ProductitemRealmModel;
import io.realm.Realm;
import io.realm.RealmResults;

public class CartManager {

    public static void checkForNull() {
        LocalDbManager.querryRealm(realm -> {
            RealmResults<ProductitemRealmModel> result = realm.where(ProductitemRealmModel.class).isNull("stock_id").findAll();
            result.deleteAllFromRealm();
        });
    }

    public static void putCartItem(ProductItemModel product) {
        LocalDbManager.querryRealm(realm -> realm.copyToRealmOrUpdate(product.convertToRealm()));
    }

    public static void removeCartItem(ProductItemModel product) {
        LocalDbManager.querryRealm(realm -> {
            RealmResults<ProductitemRealmModel> result = realm.where(ProductitemRealmModel.class).equalTo("stock_id", product.getStock_id()).findAll();
            result.deleteAllFromRealm();
        });
    }


    public static void removeCartItem(int stock_id) {
        LocalDbManager.querryRealm(realm -> {
            RealmResults<ProductitemRealmModel> result = realm.where(ProductitemRealmModel.class).equalTo("stock_id", stock_id).findAll();
            result.deleteAllFromRealm();
        });
    }

    public static void changeAnItemQty(ProductItemModel product, int count) {
        LocalDbManager.querryRealm(realm -> {
            ProductitemRealmModel result = realm.where(ProductitemRealmModel.class).equalTo("stock_id", product.getStock_id()).findFirst();
            result.setCount(count);
            if (result.getCount() == 0)
                result.deleteFromRealm();
        });
    }

    public static void changeAnItemQty(int stock_id, int count) {
        LocalDbManager.querryRealm(realm -> {
            ProductitemRealmModel result = realm.where(ProductitemRealmModel.class).equalTo("stock_id", stock_id).findFirst();
            if (result != null) {
                result.setCount(count);
                if (result.getCount() == 0)
                    result.deleteFromRealm();
            }
        });
    }

    public static void removeAnItemFromCart(ProductItemModel product) {
        LocalDbManager.querryRealm(realm -> {
            ProductitemRealmModel result = realm.where(ProductitemRealmModel.class).equalTo("stock_id", product.getStock_id()).findFirst();
            result.setCount(result.getCount() - 1);
            if (result != null) {
                if (result.getCount() == 0)
                    result.deleteFromRealm();
            }
        });
    }

    public static void addAnItemFromCart(ProductItemModel product) {
        LocalDbManager.querryRealm(realm -> {
            ProductitemRealmModel result = realm.where(ProductitemRealmModel.class).equalTo("stock_id", product.getStock_id()).findFirst();
            result.setCount(result.getCount() + 1);
        });
    }
//    public static void addAnItemFromCart(ProductItemModel product) {
//        LocalDbManager.querryRealm(realm -> {
//            ProductitemRealmModel result = realm.where(ProductitemRealmModel.class).equalTo("id", product.getId()).findFirst();
//            result.setCount(result.getCount() + 1);
//        });
//    }

    public static void removeAllCartItem() {
        LocalDbManager.querryRealm(realm -> {
            RealmResults<ProductitemRealmModel> result = realm.where(ProductitemRealmModel.class).findAll();
            result.deleteAllFromRealm();
        });
    }

    public static int getProductQty(int stock_id) {
        Realm r = Realm.getDefaultInstance();
        if (r.where(ProductitemRealmModel.class).equalTo("stock_id", stock_id).findFirst() != null) {
            return r.where(ProductitemRealmModel.class).equalTo("stock_id", stock_id).findFirst().getCount();
        }else{
            return 0;
        }
    }
}

