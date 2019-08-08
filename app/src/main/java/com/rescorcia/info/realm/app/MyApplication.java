package com.rescorcia.info.realm.app;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static Realm initRealm(Context ctx){
        // Create the Realm configuration
        //RealmConfiguration realmConfig = new RealmConfiguration.Builder(ctx).build();
        return null;
    }
}
