package cz.devconf2017;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by jridky on 11.12.16.
 */

public class AboutFragment extends Fragment {
    TextView data, title;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offline, container, false);

        title = (TextView) view.findViewById(R.id.title);
        data = (TextView) view.findViewById(R.id.data);

        title.setText(R.string.about);
        data.setText(Html.fromHtml(getString(R.string.aboutData)));
        data.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}