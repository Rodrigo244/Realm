package com.rescorcia.info.realm.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class ObtenerInfo implements Thread.UncaughtExceptionHandler {

    //Variables
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private Activity activity = null;


    //Constructor
    public ObtenerInfo(Activity activity){
        this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.activity = activity;
    }


    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {

    }


    /*Función que obtiene el IMEI de un dispositivo en Android.*/
    public static String getImei(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
