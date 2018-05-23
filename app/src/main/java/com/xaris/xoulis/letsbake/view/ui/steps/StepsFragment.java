package com.xaris.xoulis.letsbake.view.ui.steps;


import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;

import com.xaris.xoulis.letsbake.di.Injectable;

import javax.inject.Inject;

public class StepsFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
}
