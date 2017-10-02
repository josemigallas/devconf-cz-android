package cz.devconf.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Base class for Activities that manages all common logic, including views initialization.
 * All Activities should extend from this class.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getLayoutId();

    @Override
    final protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }
}
