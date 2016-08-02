package com.example.madhurarora.treebo.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by madhur.arora on 02/08/16.
 */
public class ApplicationClass extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationClass.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ApplicationClass.context;
    }
}
