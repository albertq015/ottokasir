package com.ottokonek.ottokasir.dao.manager;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LocalDbManager {

    public static void setupRealm(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("lvnmrtrlm.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

    }

    public static void querryRealm(Realm.Transaction transaction) {
        Realm r = Realm.getDefaultInstance();
        try {
            r.executeTransaction(transaction);
        } finally {
            if (!r.isClosed()) {
                r.close();
            }
        }
    }

    public static void querryRealmOnThread(Realm.Transaction transaction) {
        Thread thread = new Thread(() -> querryRealm(transaction));
        thread.start();
    }

}
