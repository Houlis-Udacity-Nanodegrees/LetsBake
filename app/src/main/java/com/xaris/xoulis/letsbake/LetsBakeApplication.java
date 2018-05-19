package com.xaris.xoulis.letsbake;

import android.app.Activity;
import android.app.Application;

import com.xaris.xoulis.letsbake.di.AppInjector;

import dagger.android.AndroidInjector;
import dagger.android.HasActivityInjector;

public class LetsBakeApplication extends Application implements HasActivityInjector {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return null;
    }
}
