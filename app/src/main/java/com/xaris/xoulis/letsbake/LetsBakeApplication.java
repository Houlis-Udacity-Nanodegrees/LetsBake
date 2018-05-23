package com.xaris.xoulis.letsbake;

import android.app.Activity;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.xaris.xoulis.letsbake.di.AppInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class LetsBakeApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        AppInjector.init(this);

        Stetho.initializeWithDefaults(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
