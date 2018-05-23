package com.xaris.xoulis.letsbake.di;

import android.app.Application;

import com.xaris.xoulis.letsbake.LetsBakeApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuildersModule.class,
        FragmentBuildersModule.class,
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(LetsBakeApplication letsBakeApplication);
}
