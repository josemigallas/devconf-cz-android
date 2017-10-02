package cz.devconf.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base class for Fragments that manages all common logic, including views initialization.
 * All Fragments should extend from this class.
 */
public abstract class BaseFragment extends Fragment {

    protected View view;
    private Unbinder unbinder;

    public abstract int getLayoutId();

    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
