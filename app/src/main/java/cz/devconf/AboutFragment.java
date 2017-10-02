package cz.devconf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cz.devconf.base.BaseFragment;

public class AboutFragment extends BaseFragment {

    @BindView(R.id.data)
    TextView data;

    @BindView(R.id.title)
    TextView title;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setText(R.string.about);
        data.setText(Html.fromHtml(getString(R.string.aboutData)));
        data.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_offline;
    }
}