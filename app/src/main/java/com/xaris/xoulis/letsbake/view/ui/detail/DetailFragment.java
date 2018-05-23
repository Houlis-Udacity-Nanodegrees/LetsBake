package com.xaris.xoulis.letsbake.view.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;

import com.xaris.xoulis.letsbake.di.Injectable;

import javax.inject.Inject;

public class DetailFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
}
